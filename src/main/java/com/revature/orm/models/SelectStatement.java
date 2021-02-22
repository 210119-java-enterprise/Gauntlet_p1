package com.revature.orm.models;

import com.revature.orm.annotations.Table;
import com.revature.orm.util.MetaModel;

import java.util.List;

/**
 * A model class that corresponds to any JDBC select statement
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class SelectStatement {

    /**
     * The String representation of the JDBC select query to be executed
     */
    private String statement;

    /**
     * Constructor for SelectStatement that creates the select statement
     * @param model the MetaModel that corresponds to the Object the user would like to grab from the database
     */
    public SelectStatement (MetaModel<?> model) {

        statement = "";

        // Get the table name. . .
        String table = model.getModeledClass().getAnnotation(Table.class).name();

        // Build the simple select statement. . .
        statement = "SELECT * FROM " + table;

    }

    /**
     * Returns the String representation of the JDBC select statement
     * @return the private statement variable
     */
    public String getStatement() {
        return statement;
    }
}
