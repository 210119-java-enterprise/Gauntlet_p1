package com.revature.orm.repos;

import com.revature.orm.models.SelectStatement;
import com.revature.orm.util.ColumnField;
import com.revature.orm.util.ConnectionFactory;
import com.revature.orm.util.MetaModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DQLRepo {

    /**
     * Grabs all of the records (Objects) that corresponds to the MetaModel and returns a List of them
     * @param model the MetaModel to pull all of the corresponding records for
     * @return a List of the Objects from the database that correspond to the given MetaModel
     */
    public List<?> selectAll (MetaModel<?> model) {

        // List of the Objects that will be obtained from the SELECT query. . .
        List<Object> objList = new ArrayList<>();

        // Create the SelectStatement object to build the SELECT statement. . .
        SelectStatement statement = new SelectStatement(model);

        // Wanting to find the noArgConstructor. . .
        Constructor<?> noArgConstructor = null;
        Constructor<?>[] constructors = model.getModeledClass().getConstructors();

        // Using streams to find the noArgConstructor. . .
        noArgConstructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterTypes().length == 0)
                .findFirst()
                .get();

        if (noArgConstructor == null) {
            throw new IllegalStateException("The class, " + model.getClassName() + ", does not have a No Args Constructor!");
        }


        // Get the connection. . . Should be a SessionFactory connection once pooling is implemented. . .
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // Prepare the statement. . .
            PreparedStatement pstmt = conn.prepareStatement(statement.getStatement());

            // Grab the ResultSet from the statement. . .
            ResultSet rs = pstmt.executeQuery();

            // List for the column names. . .
            List<String> columnNames = new ArrayList<>();

            // List of ColumnField Objects from the model. . .
            List<ColumnField> modelColumns = model.getColumns();

            // Grab the MetaData from the ResultSet. . .
            ResultSetMetaData metaData = rs.getMetaData();

            // For all of the columns in the MetaData. . .
            for (int i = 0; i < metaData.getColumnCount(); i++) {

                // Get the i+1 column since the first column would be the Id column. . .
                columnNames.add(metaData.getColumnName(i+1));
            }

            // While there are more records / objects. . .
            while (rs.next()) {

                // Create a new instance of the object from the noArgConstructor. . .
                Object obj = noArgConstructor.newInstance();

                // For each column in the column names. . .
                for (String s : columnNames) {

                    // For each ColumnField in the model. . .
                    for (ColumnField column : modelColumns) {

                        // If the columns match. . .
                        if (column.getColumnName().equals(s)) {

                            // Get the value for the column. . .
                            Object value = rs.getObject(s);

                            // Get the correct name for the column from the model. . .
                            String colName = column.getName();

                            // Get the name for the method from the model. . .
                            // Makes sure to capitalize the first letter since setters are camel case and this is the variable name. . .
                            String nameForMethod = colName.substring(0,1).toUpperCase() + colName.substring(1);

                            // Get and invoke the setter for the variable. . .
                            Method method = model.getModeledClass().getMethod("set" + nameForMethod, column.getType());
                            method.invoke(obj, value);
                        }
                    }
                }

                // Add the object to the list. . .
                objList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("An SQL Exception occurred!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An " + e.getClass().getName() + " exception occurred!");
            e.printStackTrace();
        }

        // Return all objects from the query. . .
        return objList;
    }

}
