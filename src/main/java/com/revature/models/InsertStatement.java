package com.revature.models;

import com.revature.annotations.Table;
import com.revature.util.ColumnField;
import com.revature.util.MetaModel;

import java.util.List;
import java.util.stream.Collectors;

public class InsertStatement {

    private String statement = "";

    public InsertStatement (MetaModel<?> model, Object newObj) {
        scrape(model, newObj);
    }

    private void scrape(MetaModel<?> model, Object newObj) {
        String table = newObj.getClass().getAnnotation(Table.class).name();
        List<String> columns = model.getColumnsMinusId()
                                    .stream()
                                    .map(ColumnField::getColumnName)
                                    .collect(Collectors.toList());

        buildStatement(table, columns);
    }

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
