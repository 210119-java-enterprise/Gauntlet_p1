package com.revature.repos;


import com.revature.annotations.Column;
import com.revature.models.InsertStatement;
import com.revature.util.ConnectionFactory;
import com.revature.util.MetaModel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DMLRepo {

    public void insert(MetaModel<?> model, Object obj) {

        InsertStatement statement = new InsertStatement(model, obj);
        ArrayList<String> objVal = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                try {
                    objVal.add(field.get(obj).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(statement.getStatement());

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());
            for (int i = 0; i < objVal.size(); i++) {
                pstmt.setObject(i + 1, objVal.get(i));
            }

            if(pstmt.executeUpdate() > 0)
                System.out.println("Successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
