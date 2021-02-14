package com.revature.util;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class MetaModel<T> {

    private Class<T> clazz;
    private IdField primaryKey;
    private List<ColumnField> columns;

    public static <T> MetaModel<T> of(Class<T> clazz) {
        if (clazz.getAnnotation(Entity.class) == null) {
            throw new IllegalStateException("Cannot create MetaModel object! Provided class, " + clazz.getName() + ", is not annotated with @Entity.");
        }
        return new MetaModel<>(clazz);
    }

    public MetaModel (Class<T> clazz) {
        this.clazz = clazz;
        this.columns = new LinkedList<>();
    }

    public String getClassName() {
        return clazz.getName();
    }

    public String getSimpleClassName() {
        return clazz.getSimpleName();
    }

    public IdField getPrimaryKey() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id primaryK = field.getAnnotation(Id.class);
            if (primaryK != null) {
                return new IdField(field);
            }
        }
        // Should this be RunTimeException??
        throw new RuntimeException("Did not find a field annotated with @Id in: " + clazz.getName());
    }

    public List<ColumnField> getColumns() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columns.add(new ColumnField(field));
            }
        }

        if (columns.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        return columns;
    }

    public List<ColumnField> getColumnsMinusId() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {
                columns.add(new ColumnField(field));
            }
        }

        if (columns.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        return columns;
    }
}
