package com.revature.util;

import com.revature.annotations.Id;

import java.lang.reflect.Field;

public class IdField {

    private Field field;

    public IdField (Field field) {

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
