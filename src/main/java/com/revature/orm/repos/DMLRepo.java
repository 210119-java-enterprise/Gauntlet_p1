package com.revature.orm.repos;

import com.revature.orm.annotations.Column;
import com.revature.orm.annotations.Id;
import com.revature.orm.models.DeleteStatement;
import com.revature.orm.models.InsertStatement;
import com.revature.orm.models.SelectStatement;
import com.revature.orm.models.UpdateStatement;
import com.revature.orm.util.ColumnField;
import com.revature.orm.util.ConnectionFactory;
import com.revature.orm.util.MetaModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Repository class for handling any JDBC Data Manipulation Language Queries
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class DMLRepo {

    /**
     * Inserts the Object into the database
     * @param model the MetaModel that corresponds to the Object that is being inserted into the database
     * @param obj the Object that contains the values that should be inserted into the database
     * @return a boolean that is true if the insert happened and false if it didn't
     */
    public int insert(MetaModel<?> model, Object obj) {

        InsertStatement statement = new InsertStatement(model, obj);
        ArrayList<Object> objVal = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        int newId = -1;

        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {
                try {
                    objVal.add(field.get(obj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(statement.getStatement());

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement(), Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < objVal.size(); i++) {
                pstmt.setObject(i + 1, objVal.get(i));
            }

            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Rows: " + rowsInserted);

            if (rowsInserted != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newId < 0) {
            // throw exception...
        }

        return newId;
    }

    /**
     * Deletes a record from the database that corresponds to the values in the Object
     * @param deleteObj the Object that should be deleted from the database
     * @return a boolean that is true if the Object was deleted or false if it is not
     */
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

    /**
     * Updates a record in the database with new values where the old values existed
     * @param model the MetaModel corresponding to the updateObj and oldObj
     * @param updateObj the Object that holds the new values to be updated into the record
     * @param oldObj the Object that holds the current values to find in the record
     * @return a boolean that is true if a record was updated or false if it was not
     */
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

    /**
     * Grabs all of the records (Objects) that corresponds to the MetaModel and returns a List of them
     * @param model the MetaModel to pull all of the corresponding records for
     * @return a List of the Objects from the database that correspond to the given MetaModel
     */
    public List<?> selectAll (MetaModel<?> model) {

        List<Object> objList = new ArrayList<>();
        SelectStatement statement = new SelectStatement(model);
        Constructor<?> noArgConstructor = null;
        Constructor<?>[] constructors = model.getModeledClass().getConstructors();

        System.out.println(model.getModeledClass().getName());
        System.out.println(constructors.length);

        noArgConstructor = Arrays.stream(constructors)
                                 .filter(c -> c.getParameterTypes().length == 0)
                                 .findFirst()
                                 .get();


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());
            ResultSet rs = pstmt.executeQuery();

            List<String> columnNames = new ArrayList<>();
            List<ColumnField> modelColumns = model.getColumns();
            ResultSetMetaData metaData = rs.getMetaData();

            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columnNames.add(metaData.getColumnName(i+1));
            }

            while (rs.next()) {
                Object obj = noArgConstructor.newInstance();


                for (String s : columnNames) {
                    for (ColumnField column : modelColumns) {
                        if (column.getColumnName().equals(s)) {
                            Object value = rs.getObject(s);
                            String colName = column.getName();
                            String nameForMethod = colName.substring(0,1).toUpperCase() + colName.substring(1);
                            Method method = model.getModeledClass().getMethod("set" + nameForMethod, column.getType());
                            method.invoke(obj, value);
                        }
                    }
                }

                objList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objList;
    }

    // TODO
    public List<?> selectWhere (MetaModel<?> model, List<ColumnField> columns) {
        List<Object> objList = new ArrayList<>();
        List<String> columnNames = columns.stream()
                                        .map(ColumnField::getName)
                                        .collect(Collectors.toList());

        SelectStatement statement = new SelectStatement(model, columnNames);
        Constructor<?> noArgConstructor = null;
        Constructor<?>[] constructors = model.getModeledClass().getConstructors();

        System.out.println(model.getModeledClass().getName());
        System.out.println(constructors.length);

        noArgConstructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterTypes().length == 0)
                .findFirst()
                .get();


        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());
            ResultSet rs = pstmt.executeQuery();

            List<String> objColumnNames = new ArrayList<>();
            List<ColumnField> modelColumns = model.getColumns();
            ResultSetMetaData metaData = rs.getMetaData();

            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columnNames.add(metaData.getColumnName(i+1));
            }

            while (rs.next()) {
                Object obj = noArgConstructor.newInstance();


                for (String s : objColumnNames) {
                    for (ColumnField column : modelColumns) {
                        if (column.getColumnName().equals(s)) {
                            Object value = rs.getObject(s);
                            String colName = column.getName();
                            String nameForMethod = colName.substring(0,1).toUpperCase() + colName.substring(1);
                            Method method = model.getModeledClass().getMethod("set" + nameForMethod, column.getType());
                            method.invoke(obj, value);
                        }
                    }
                }

                objList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objList;
    }

}
