package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.annotations.Table;
import com.revature.util.MetaModel;

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

    public void scrape (MetaModel<?> model, Object updateObj) {
        String table = updateObj.getClass().getAnnotation(Table.class).name();
        List<String> columns = new ArrayList<>();
        Field[] fields = updateObj.getClass().getDeclaredFields();

        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);

            if (id == null) {
                Column column = field.getAnnotation(Column.class);
                columns.add(column.name());
            }
        }

        buildStatement(table, columns);
    }

    // Trying with StringBuilder...
    private void buildStatement (String table, List<String> columns) {
        StringBuilder updateString = new StringBuilder("SET ");
        StringBuilder whereString = new StringBuilder("WHERE ");

        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size() - 1) {
                updateString.append(columns.get(i)).append(" = ? ");
                whereString.append((columns.get(i))).append(" = ? ");
            } else {
                updateString.append(columns.get(i)).append(" = ? , ");
                whereString.append(columns.get(i)).append(" = ? and ");
            }

            statement = "UPDATE " + table + " " + updateString.toString() + whereString.toString();
        }
    }

    public String getStatement() {
        return statement;
    }
}
