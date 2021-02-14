package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionFactory encapsulates the properties and the implementation of the JDBC connection to the database
 */
public class ConnectionFactory {

    /**
     * ConnectionFactory object for the application
     */
    private static ConnectionFactory connFactory = new ConnectionFactory();


    /**
     * The properties of the application that allows the application to connect / login to the database
     */
    private Properties props = new Properties();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    /**
     * Constructor for the ConnectionFactory. Loads the properties file.
     */
    private ConnectionFactory () {
        try {
            props.load(new FileReader("src/main/resources/app.properties"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Gets the ConnectionFactory for the application
     * @return the static ConnectionFactory for the application
     */
    public static ConnectionFactory getInstance () {
        return connFactory;
    }

    /**
     * Gets the connection to the database using the properties from the Properties object
     * @return the connection to the database
     */
    public Connection getConnection () {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("admin-usr"),
                    props.getProperty("admin-pw")
            );
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return conn;
    }

}
