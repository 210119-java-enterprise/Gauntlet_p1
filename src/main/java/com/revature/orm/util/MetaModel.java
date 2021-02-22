package com.revature.orm.util;

import com.revature.orm.annotations.Column;
import com.revature.orm.annotations.Entity;
import com.revature.orm.annotations.Id;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents an Annotated Class
 * @param <T> the Type of the Object that will be represented
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class MetaModel<T> {

    // The Class of the Object. . .
    private Class<T> clazz;

    // The IdField object corresponding to the primary key. . .
    private IdField primaryKey;

    // The List of ColumnField objects corresponding to the columns in the database. . .
    private List<ColumnField> columns;

    /**
     * Checks to see if a class is an Entity, then creates a MetaModel of the class
     * @param clazz the class to model
     * @param <T> the Type of the class
     * @return a MetaModel of the class
     */
    public static <T> MetaModel<T> of(Class<T> clazz) {

        // If the class is not annotated with Entity, it is not fully annotated. . .
        if (clazz.getAnnotation(Entity.class) == null) {
            throw new IllegalStateException("Cannot create MetaModel object! Provided class, " + clazz.getName() + ", is not annotated with @Entity.");
        }
        // Return the MetaModel. . .
        return new MetaModel<>(clazz);
    }

    /**
     * Constructor for MetaModel
     * @param clazz the class to model
     */
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

    /**
     * Find the primary key field of a class
     * @return the IdField that corresponds to the primary key
     */
    public IdField getPrimaryKey() {

        // Get the fields of the class. . .
        Field[] fields = clazz.getDeclaredFields();

        // Check each field for the Id annotation. . .
        for (Field field : fields) {
            Id primaryK = field.getAnnotation(Id.class);
            if (primaryK != null) {
                return new IdField(field);
            }
        }

        // Exception if there was no field with Id annotation. . .
        throw new RuntimeException("Did not find a field annotated with @Id in: " + clazz.getName());
    }

    /**
     * Gets the fields annotated with a Column annotation
     * @return the List of ColumnFields for a class
     */
    public List<ColumnField> getColumns() {

        // Get all fields for the class. . .
        Field[] fields = clazz.getDeclaredFields();

        // For each field, check if it has a Column annotation. . .
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {

                // Add if there is  a Column annotation on this field. . .
                columns.add(new ColumnField(field));
            }
        }

        // No columns were found. . .
        if (columns.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        // Return all of the correctly annotated columns. . .
        return columns;
    }

    /**
     * Returns all of the columns except the column annotated with Id
     * @return a List of ColumnFields
     */
    public List<ColumnField> getColumnsMinusId() {

        // Get all fields. . .
        Field[] fields = clazz.getDeclaredFields();

        // For each field, check for Column and Id annotation. . .
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);

            // If it is not annotated with Id and is annotated with Column, add. . .
            if ((id == null) && (column != null)) {
                columns.add(new ColumnField(field));
            }
        }

        // No columns found. . .
        if (columns.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        // Return the columns with no Id annotation. . .
        return columns;
    }

    /**
     * Grabs the class that the MetaModel models
     * @return a Class
     */
    public Class<?> getModeledClass() {
        return clazz;
    }
}
