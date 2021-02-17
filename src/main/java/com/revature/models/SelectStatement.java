package com.revature.models;

import com.revature.annotations.Table;
import com.revature.util.MetaModel;

public class SelectStatement {

    private String statement = "";

    public SelectStatement (MetaModel<?> model) {

        //String table = model.getClass().getAnnotation(Table.class).name();

        String table = "app_users";

        statement = "SELECT * FROM " + table;

    }

    public String getStatement() {
        return statement;
    }
}
