package com.revature.repos;


import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.models.DeleteStatement;
import com.revature.models.InsertStatement;
import com.revature.models.UpdateStatement;
import com.revature.util.ConnectionFactory;
import com.revature.util.MetaModel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DMLRepo {

    public boolean insert(MetaModel<?> model, Object obj) {

        InsertStatement statement = new InsertStatement(model, obj);
        ArrayList<String> objVal = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean isSuccessful = false;

        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {
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
                isSuccessful = true;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public boolean delete (Object deleteObj) {
        DeleteStatement statement = new DeleteStatement(deleteObj);
        System.out.println(statement.getStatement());
        int idVal = 0;
        Field[] fields = deleteObj.getClass().getDeclaredFields();
        boolean isSuccessful = false;

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                try {
                    idVal = field.getInt(deleteObj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());
            pstmt.setInt(1, idVal);

            if (pstmt.executeUpdate() > 0)
                isSuccessful = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public boolean update (MetaModel<?> model, Object updateObj, Object oldObj) {

        UpdateStatement statement = new UpdateStatement(model, oldObj);
        System.out.println(statement.getStatement());
        boolean isSuccessful = false;

        List<String> oldVal = new ArrayList<>();
        List<String> newVal = new ArrayList<>();

        Field[] oldFields = oldObj.getClass().getDeclaredFields();
        Field[] newFields = updateObj.getClass().getDeclaredFields();

        for (Field field : oldFields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {
                try {
                    oldVal.add(field.get(oldObj).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (Field field : newFields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {
                try {
                    newVal.add(field.get(updateObj).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        int numCol = oldVal.size();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());

            for (int i = 0; i < numCol; i++) {
                pstmt.setObject(i+1, newVal.get(i));
                pstmt.setObject(i+numCol+1, oldVal.get(i));
            }

            if (pstmt.executeUpdate() > 0)
                isSuccessful = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

}
