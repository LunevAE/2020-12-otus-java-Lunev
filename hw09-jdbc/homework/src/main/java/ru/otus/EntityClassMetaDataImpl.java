package ru.otus;

import ru.otus.core.meta.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl implements EntityClassMetaData {

    private final String className;
    private final Constructor<?> constructor;
    private final Field fieldId;
    private final List<Field> fields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.className = clazz.getSimpleName();
        try {
            this.constructor = clazz.getConstructor();
            this.fieldId = Arrays.stream(clazz.getDeclaredFields())
                    .filter(it -> it.isAnnotationPresent(Id.class))
                    .findFirst().orElseThrow(RuntimeException::new);
            this.fields = Arrays.stream(clazz.getDeclaredFields())
                    .sorted(Comparator.comparing(Field::getName))
                    .collect(Collectors.toList());
            this.fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                    .filter(it -> !it.isAnnotationPresent(Id.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Constructor<T> getConstructor() {
        return (Constructor<T>) constructor;
    }

    @Override
    public Field getIdField() {
        return fieldId;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
