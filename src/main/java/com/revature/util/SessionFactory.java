package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class SessionFactory {

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private Connection conn;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public SessionFactory (String dbUrl, String dbUsername, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername=  dbUsername;
        this.dbPassword = dbPassword;

        try {
            conn = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Should go into a Session.
    public Connection getConn() {
        return conn;
    }
}
