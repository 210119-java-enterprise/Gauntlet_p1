package com.revature.orm.util;

import com.revature.orm.annotations.Column;

import java.lang.reflect.Field;

/**
 * Class that corresponds to a column in the database
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class ColumnField {

    /**
     * Field object that corresponds to the column field
     */
    private Field field;

    /**
     * Column Field constructor
     * @param field the field that corresponds to a column in the database
     */
    public ColumnField (Field field) {

        // Field not annotated with Column. . .
        if (field.getAnnotation(Column.class) == null) {
            throw new IllegalStateException("Cannot create ColumnField object! Provided field, " + field.getName() + ", is not annotated with @Column.");
        }

        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    /**
     * Gets the column name from the annotation
     * @return a String for column name
     */
    public String getColumnName() {
        return field.getAnnotation(Column.class).name();
    }

}

