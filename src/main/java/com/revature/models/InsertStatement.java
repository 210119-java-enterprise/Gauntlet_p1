package com.revature.models;

import com.revature.annotations.Table;
import com.revature.util.ColumnField;
import com.revature.util.MetaModel;

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
        String table = newObj.getClass().getAnnotation(Table.class).name();
        List<String> columns = model.getColumnsMinusId()
                                    .stream()
                                    .map(ColumnField::getColumnName)
                                    .collect(Collectors.toList());

        buildStatement(table, columns);
    }

    /**
     * Private helper method that builds the statement
     * @param table a String for the table name to include into the insert statement
     * @param columns a List of columns to include in the insert statement
     */
    private void buildStatement (String table, List<String> columns) {

        statement = "INSERT INTO " + table + " (";
        String valuesStr = " VALUES (";

        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size() - 1) {

                statement += columns.get(i) + ") ";
                valuesStr += " ? ) ";
            } else {

                statement += columns.get(i) + ", ";
                valuesStr += " ? , ";
            }
        }

        statement += valuesStr;
    }

    public String getStatement() {
        return statement;
    }
}
