package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.annotations.Table;
import com.revature.util.ColumnField;
import com.revature.util.MetaModel;

import java.lang.reflect.Field;
import java.util.List;

public class DeleteStatement {

    private String statement = "";

    public DeleteStatement (Object deleteObj) {
        scrape(deleteObj);
    }

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

    public void buildStatement (String table, String idColName) {
        statement += "DELETE FROM " + table +
                    " WHERE " + idColName + " = ?";
    }

    public String getStatement() {
        return statement;
    }
}
