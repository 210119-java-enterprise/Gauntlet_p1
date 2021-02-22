package com.revature.orm.util;

import java.io.FileReader;
import java.util.*;

/**
 * Configuration class that will build the SessionFactory Object based on the properties provided by the User.
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class Configuration {

    /**
     * A List of MetaModels corresponding to the Annotated Classes
     */
    private List<MetaModel<Class<?>>> metaModels;

    /**
     * Properties from the given properties files from the User
     */
    private Properties props = new Properties();

    /**
     * Database URL String
     */
    private String dbUrl;

    /**
     * Database User String
     */
    private String dbUser;

    /**
     * Database Password String
     */
    private String dbPass;


    /**
     * Configuration Constructor that takes in a path to the properties for the database
     * @param path the String path to the properties file
     */
    public Configuration(String path) {

        // Get the properties file. . .
        try {
            props.load(new FileReader(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the properties. . .
        dbUrl = props.getProperty("url");
        dbUser = props.getProperty("admin-usr");
        dbPass = props.getProperty("admin-pw");
    }

    /**
     * Adds an annotated class to the MetaModels list or instantiates a new List if there is not one
     * @param annotatedClass the class to be added
     * @return the Configuration object (allows chaining)
     */
    @SuppressWarnings({"unchecked"})
    public Configuration addAnnotatedClass (Class annotatedClass) {

        // If no list, make a List. . .
        if (metaModels == null) {
            metaModels = new LinkedList<>();
        }

        // Add the MetaModel of the class. . .
        metaModels.add(MetaModel.of(annotatedClass));

        // Return Configuration Object. . .
        return this;
    }

    /**
     * Gets the list of the MetaModels for the classes that have been added with the .addAnnotatedClasses method
     * @return a List of the MetaModels or an empty list
     */
    public List<MetaModel<Class<?>>> getMetaModels() {
        return (metaModels == null) ? Collections.emptyList() : metaModels;
    }

    /**
     * Builds a SessionFactory Object with the MetaModels
     * @return the SessionFactory that was built
     */
    public SessionFactory buildSessionFactory () {
        return new SessionFactory(metaModels);
    }

}
