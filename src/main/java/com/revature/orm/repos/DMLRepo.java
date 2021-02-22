package com.revature.orm.repos;

import com.revature.orm.annotations.Column;
import com.revature.orm.annotations.Id;
import com.revature.orm.models.DeleteStatement;
import com.revature.orm.models.InsertStatement;
import com.revature.orm.models.UpdateStatement;
import com.revature.orm.util.ConnectionFactory;
import com.revature.orm.util.MetaModel;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * @return an int that corresponds to the new Id value of the record, or a -1 if nothing was inserted.
     */
    public int insert(MetaModel<?> model, Object obj) {

        // Create the InsertStatement Object that will build the String representation of the INSERT statement. . .
        InsertStatement statement = new InsertStatement(model, obj);

        // An ArrayList for the values of the Object to be inserted. . .
        ArrayList<Object> objVal = new ArrayList<>();

        // Get the fields of the Object passed in. . .
        Field[] fields = obj.getClass().getDeclaredFields();

        // Id value for the new record of the database. . .
        int newId = -1;

        // For each field of the Object. . .
        for (Field field : fields) {

            // Make sure we can access private fields. . .
            field.setAccessible(true);

            // See if the field is annotated with column and Id. . .
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);

            // If it is a column, but not the Id. . .
            if ((id == null) && (column != null)) {


                try {

                    // Add the value of that column to the Object values ArrayList. . .
                    objVal.add(field.get(obj));
                } catch (IllegalAccessException e) {
                    System.out.println("The variable was not able to be accessed. Was field.getAccessible set to true?");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Get connection. . . Should be replaced with SessionFactory Connection once Connection Pooling is implemented. . .
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // Prepare the statement to the connection while generating keys to get the new Id. . .
            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement(), Statement.RETURN_GENERATED_KEYS);

            // Add the Object values to the query. . .
            for (int i = 0; i < objVal.size(); i++) {
                pstmt.setObject(i + 1, objVal.get(i));
            }

            // Should only be 1. . .
            int rowsInserted = pstmt.executeUpdate();

            // If there was a row inserted, get the generated keys and set the newId from the ResultSet. . .
            if (rowsInserted != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred. Check the SQL Strings or data");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An exception occurred that was not SQL.");
            e.printStackTrace();
        }

        if (newId < 0) {
            throw new RuntimeException("New Generated Id was not received.");
        }

        // Return the Id of the new record. . .
        return newId;
    }

    /**
     * Deletes a record from the database that corresponds to the values in the Object
     * @param deleteObj the Object that should be deleted from the database
     * @return a boolean that is true if the Object was deleted or false if it is not
     */
    public boolean delete (Object deleteObj) {

        // Create the DeleteStatement object that will build the JDBC DELETE statement String.
        DeleteStatement statement = new DeleteStatement(deleteObj);

        // Id of the object to delete. . .
        int idVal = 0;

        // Fields of the deleteObj's class. . .
        Field[] fields = deleteObj.getClass().getDeclaredFields();

        // Did we delete. . .
        boolean isSuccessful = false;

        // For each field. . .
        for (Field field : fields) {

            // Make sure we can use private fields. . .
            field.setAccessible(true);

            // If the field has the Id annotation. . .
            if (field.getAnnotation(Id.class) != null) {

                // Set idVal equal to the Id of the deleteObj. . .
                try {
                    idVal = field.getInt(deleteObj);
                } catch (IllegalAccessException e) {
                    System.out.println("A field was unable to be accessed. Was field.getAccessible set to true?");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Get the connection. . . Should be SessionFactory Connection when implemented. . .
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // Prepare the DELETE statement. . .
            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());
            pstmt.setInt(1, idVal);

            // If a record was deleted, isSuccessfull is true. . .
            if (pstmt.executeUpdate() > 0)
                isSuccessful = true;

        } catch (SQLException e) {
            System.out.println("An SQL Exception occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if there was a deletion. . .
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

        // Create the UpdateStatement object that will build the String for the JDBC UPDATE query. . .
        UpdateStatement statement = new UpdateStatement(model, oldObj);

        // Did we update a record. . .
        boolean isSuccessful = false;

        // Create a List<Object> of values from both the old Object and the updated Object. . .
        List<Object> oldVal = getFieldValues(oldObj);
        List<Object> newVal = getFieldValues(updateObj);

        // Columns in each object. . .
        int numCol = oldVal.size();

        // Get the connection. . . Should be a SessionFactory Connection when pooling is implemented. . .
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // Prepare the UPDATE statement. . .
            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());

            // For every column in each object. . .
            for (int i = 0; i < numCol; i++) {

                // Set the new values for the record. . .
                pstmt.setObject(i+1, newVal.get(i));

                // Where the old values equal. . .
                // This is parameter i+numCol+1 since we know that the objects should have the same number of fields / columns. . .
                pstmt.setObject(i+numCol+1, oldVal.get(i));
            }

            // If there was an updated record. . .
            if (pstmt.executeUpdate() > 0)
                isSuccessful = true;

        } catch (SQLException e) {
            System.out.println("SQL Exception occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return if there was an updated record. . .
        return isSuccessful;
    }

    /**
     * Helper method to get all of the values in an Object List based of off the object passed in
     * @param obj the object to retrieve the values from
     * @return a List of the values of the fields in the object
     */
    private List<Object> getFieldValues (Object obj) {

        // List of the values of the fields for the object. . .
        List<Object> values = new ArrayList<>();

        // Get the fields of the object. . .
        Field[] objFields = obj.getClass().getDeclaredFields();

        // For each field. . .
        for (Field field : objFields) {

            // Make sure we can use private fields. . .
            field.setAccessible(true);

            // Check if it is a column and not an Id. . .
            Column column = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if ((id == null) && (column != null)) {

                // Add the values to the values List. . .
                try {
                    values.add(field.get(obj));
                } catch (IllegalAccessException e) {
                    System.out.println("A field was unable to be accessed. Was field.getAccessible set to true?");
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return values;
    }
}
