package com.revature.models;

import com.revature.annotations.Table;
import com.revature.util.MetaModel;

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

        String table = model.getModeledClass().getAnnotation(Table.class).name();

        statement = "SELECT * FROM " + table;

    }

    public String getStatement() {
        return statement;
    }
}
