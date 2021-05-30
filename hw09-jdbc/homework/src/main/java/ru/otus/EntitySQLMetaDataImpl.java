package ru.otus;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        var selectFields = entityClassMetaData.getAllFields().stream()
                .map(it -> it.getName().toLowerCase()).collect(Collectors.joining(", "));
        return String.format("SELECT %s FROM %s WHERE %s = ?",
                selectFields,
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var insertFields = fieldsWithoutId.stream()
                .map(it -> it.getName().toLowerCase()).collect(Collectors.joining(", "));

        final String marks;
        if (fieldsWithoutId.size() == 1) {
            marks = "?";
        } else {
            marks = "?" + ", ?".repeat(fieldsWithoutId.size());
        }

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                entityClassMetaData.getName().toLowerCase(),
                insertFields,
                marks);
    }

    @Override
    public String getUpdateSql() {
        var updateFields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(it -> it.getName().toLowerCase() + " = ?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName().toLowerCase(),
                updateFields,
                entityClassMetaData.getIdField().getName().toLowerCase());
    }
}
