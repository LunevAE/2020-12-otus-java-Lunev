package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    clientList.add(createInstance(rs));
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var values = extractValues(object);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var values = extractValues(object);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> extractValues(T object) throws IllegalAccessException {
        var fields = entityClassMetaData.getFieldsWithoutId();
        var values = new ArrayList<>(fields.size());

        for (var field : fields) {
            try {
                field.setAccessible(true);
                values.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw e;
            } finally {
                field.setAccessible(false);
            }
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    private T createInstance(java.sql.ResultSet rs) throws Exception {
        var instance = entityClassMetaData.getConstructor().newInstance();

        for (Field field : entityClassMetaData.getAllFields()) {
            var value = rs.getObject(field.getName());
            try {
                field.setAccessible(true);
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                throw e;
            } finally {
                field.setAccessible(false);
            }
        }

        return (T) instance;
    }
}
