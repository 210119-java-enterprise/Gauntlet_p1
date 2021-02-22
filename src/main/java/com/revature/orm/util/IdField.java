package com.revature.orm.util;

import com.revature.orm.annotations.Id;

import java.lang.reflect.Field;

/**
 * Class that represents a field that is the Id column in the database
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class IdField {

    /**
     * Field object that should correspond to the Id field
     */
    private Field field;

    /**
     * Id Field constructor
     * @param field the Field object that corresponds to the Id field
     */
    public IdField (Field field) {

        // Field isn't annotated with Id
        if (field.getAnnotation(Id.class) == null) {
            throw new IllegalStateException("Cannot create IdField object! Provided field, " + field.getName() + " is not annotated with @Id.");
        }

        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }
}
