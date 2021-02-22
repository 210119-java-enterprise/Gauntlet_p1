package com.revature.orm.models;

import com.revature.orm.annotations.Column;
import com.revature.orm.annotations.Id;
import com.revature.orm.annotations.Table;
import com.revature.orm.util.MetaModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A model class that corresponds to any JDBC update statement
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class UpdateStatement {

    /**
     * The String representation of the JDBC update query to be executed
     */
    private String statement = "";

    /**
     * Constructor for UpdateStatement that scrapes and builds the statement from a MetaModel and Object
     * @param model the MetaModel that corresponds to the updateObj
     * @param updateObj the Object that will provide the new values for the record in the database
     */
    public UpdateStatement (MetaModel<?> model, Object updateObj) {
        scrape(model, updateObj);
    }

    /**
     * Scrapes the MetaModel and Object for the table name and columns needed for update
     * @param model the MetaModel that corresponds to the updateObj
     * @param updateObj the Object that will provide the new values to update for the database
     */
    public void scrape (MetaModel<?> model, Object updateObj) {

        // Get the table name. . .
        String table = updateObj.getClass().getAnnotation(Table.class).name();

        // Start a List of Strings for column names. . .
        List<String> columns = new ArrayList<>();

        // Get the fields of the updateObj's class. . .
        Field[] fields = updateObj.getClass().getDeclaredFields();

        // For each field, check if the Id annotation. . .
        for (Field field : fields) {

            Id id = field.getAnnotation(Id.class);
            if (id == null) {

                // Get the column name if not annotated with Id. . .
                Column column = field.getAnnotation(Column.class);
                columns.add(column.name());
            }
        }

        // Build the update statement from the table name and column names. . .
        buildStatement(table, columns);
    }


    /**
     * Builds the String representation of the JDBC query
     * @param table the name of the table that will be updated
     * @param columns a List of column names that will be updated
     */
    private void buildStatement (String table, List<String> columns) {

        // Initial Strings for the StringBuilders. . .
        StringBuilder updateString = new StringBuilder("SET ");
        StringBuilder whereString = new StringBuilder("WHERE ");

        for (int i = 0; i < columns.size(); i++) {

            // If the last column. . .
            if (i == columns.size() - 1) {

                // Append the corresponding StringBuilder with column names and value representation (?). . .
                updateString.append(columns.get(i)).append(" = ? ");
                whereString.append((columns.get(i))).append(" = ? ");
            } else {

                // Append the corresponding StringBuilder with column names and value representation (?). . .
                // Includes a comma for multiple columns to SET. . .
                // Includes ands for multiple columns in the WHERE clause. . .
                updateString.append(columns.get(i)).append(" = ? , ");
                whereString.append(columns.get(i)).append(" = ? and ");
            }

            // Build the UPDATE statement. . .
            statement = "UPDATE " + table + " " + updateString.toString() + whereString.toString();
        }
    }

    /**
     * Returns the String representation of the JDBC UPDATE statement
     * @return the private statement variable
     */
    public String getStatement() {
        return statement;
    }
}
