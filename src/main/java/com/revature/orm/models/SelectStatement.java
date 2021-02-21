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

        String table = model.getModeledClass().getAnnotation(Table.class).name();

        statement = "SELECT * FROM " + table;

    }

    // Select + WHERE
    public SelectStatement (MetaModel<?> model, List<String> columnNames) {

        statement = "";

        StringBuilder where = new StringBuilder();
        where.append(" WHERE ");

        String table = model.getModeledClass().getAnnotation(Table.class).name();

        statement = "SELECT * FROM" + table;

        for (int i = 0; i < columnNames.size(); i++) {

            if (i == columnNames.size() - 1) {
                where.append(columnNames.get(i) + " = ?");
            } else {
                where.append(columnNames.get(i) + " = ?, ");
            }
        }

        statement += " " + where.toString();
    }

    public String getStatement() {
        return statement;
    }
}
