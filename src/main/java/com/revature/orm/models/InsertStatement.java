package com.revature.orm.models;

import com.revature.orm.annotations.Table;
import com.revature.orm.util.ColumnField;
import com.revature.orm.util.MetaModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A model class that corresponds to any JDBC insert statement
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class InsertStatement {

    /**
     * The String representation of the JDBC query to be executed
     */
    private String statement = "";

    /**
     * InsertStatement Constructor that needs an Object and a MetaModel that corresponds to that Object
     * @param model the MetaModel that corresponds to the Object that is being inserted
     * @param newObj the Object that holds the values to be inserted into the database
     */
    public InsertStatement (MetaModel<?> model, Object newObj) {
        scrape(model, newObj);
    }

    /**
     * Scrapes the model and newObj for the table name and columns that should be used for the insert statement
     * Then builds the statement
     * @param model the MetaModel that corresponds to the newObj
     * @param newObj the Object that holds the values to be inserted into the database
     */
    private void scrape(MetaModel<?> model, Object newObj) {

        // Get the table name. . .
        String table = newObj.getClass().getAnnotation(Table.class).name();

        // Get a list of Strings that represent the column names (without the Id column). . .
        List<String> columns = model.getColumnsMinusId()
                                    .stream()
                                    .map(ColumnField::getColumnName)
                                    .collect(Collectors.toList());

        // Build the insert statement using the table name and the column names. . .
        buildStatement(table, columns);
    }

    /**
     * Private helper method that builds the statement using String concatenation
     * @param table a String for the table name to include into the insert statement
     * @param columns a List of columns to include in the insert statement
     */
    private void buildStatement (String table, List<String> columns) {

        // Base Strings. . .
        statement = "INSERT INTO " + table + " (";
        String valuesStr = " VALUES (";

        for (int i = 0; i < columns.size(); i++) {

            // If the last column. . .
            if (i == columns.size() - 1) {

                // Add the column name to the statement and a value representation (?) to the values. . .
                // End the Strings with ) since this is the last column. . .
                statement += columns.get(i) + ") ";
                valuesStr += " ? ) ";
            } else {

                // Add the column name to the statement, and a value representation (?) to the values. . .
                statement += columns.get(i) + ", ";
                valuesStr += " ? , ";
            }
        }

        // Add the values section of the JDBC String to the rest of the statement. . .
        statement += valuesStr;
    }

    /**
     * Returns the String representation of the JDBC insert statement
     * @return the private statement variable
     */
    public String getStatement() {
        return statement;
    }
}
