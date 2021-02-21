package com.revature.orm.models;

import com.revature.orm.annotations.Column;
import com.revature.orm.annotations.Id;
import com.revature.orm.annotations.Table;

import java.lang.reflect.Field;

/**
 * A model class that corresponds to any JDBC insert statement
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class DeleteStatement {

    /**
     * The String representation of the JDBC delete query to be executed
     */
    private String statement = "";

    /**
     * Constructor for DeleteStatement that creates the JDBC string for the delete query from deleteObj
     * @param deleteObj the Object to be deleted
     */
    public DeleteStatement (Object deleteObj) {
        scrape(deleteObj);
    }

    /**
     * Scrapes the Object provided to get the table and the Id column
     * Then creates the delete statement
     * @param deleteObj the Object to be deleted
     */
    public void scrape (Object deleteObj) {
        String table = deleteObj.getClass().getAnnotation(Table.class).name();
        Field[] fields = deleteObj.getClass().getDeclaredFields();

        String idColName = "";

        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                idColName = field.getAnnotation(Column.class).name();
            }
        }

        buildStatement(table, idColName);

    }

    /**
     * Builds the delete JDBC statement from the table name and the name of the Id column of the Object
     * @param table the table to delete from
     * @param idColName the column name for the Id of the Object
     */
    private void buildStatement (String table, String idColName) {
        statement += "DELETE FROM " + table +
                    " WHERE " + idColName + " = ?";
    }

    public String getStatement() {
        return statement;
    }
}
