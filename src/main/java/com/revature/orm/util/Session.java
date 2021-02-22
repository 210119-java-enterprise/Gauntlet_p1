package com.revature.orm.util;

import com.revature.orm.services.SQLService;

import java.sql.Connection;
import java.util.List;

/**
 * Abstracts JDBC away from the User
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class Session {

    /**
     * List of MetaModels from Annotated Class passed in by the SessionFactory
     */
    private List<MetaModel<Class<?>>> modelList;

    /**
     * Service class that will handle JDBC
     */
    private SQLService SQLService;

    /**
     * Connection to the database
     */
    private Connection conn;

    /**
     * Constructor for the Session class
     * @param modelList MetaModel List to be passed by the SessionFactory
     * @param SQLService service class to handle JDBC
     * @param conn connection to the database
     */
    public Session (List<MetaModel<Class<?>>> modelList, SQLService SQLService, Connection conn) {
        this.modelList = modelList;
        this.SQLService = SQLService;
        this.conn = conn;
    }

    /**
     * Inserts an object into the database
     * @param newObj the object to be saved
     * @return the new generated int Id of the object
     */
    public int save (Object newObj) {

        // MetaModel of the class. . .
        MetaModel<?> correctModel = null;

        // Finding the correct model. . .
        for (MetaModel<?> model : modelList) {
            if (newObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        // MetaModel not found (not an Annotated Class). . .
        if (correctModel == null) {
            // throw exception...
        }

        // Insert the object. . .
        return SQLService.insert(correctModel, newObj);
    }

    /**
     * Deletes the given object from the database
     * @param deleteObj the object to be deleted
     * @return a boolean depending on if the object was deleted successfully
     */
    public boolean delete (Object deleteObj) {

        // Delete the object. . .
        return SQLService.delete(deleteObj);

    }

    /**
     * Updates the record of the object in the datatbase
     * @param updateObj the object with new values for the record
     * @param oldObj the object with the old values of the record
     * @return if an update occurred
     */
    public boolean update(Object updateObj, Object oldObj) {

        // Classes are not the same. . .
        if(!updateObj.getClass().equals(oldObj.getClass())) {
            // throw exception...
        }

        // MetaModel for the class. . .
        MetaModel<?> correctModel = null;

        // Find the MetaModel. . .
        for (MetaModel<?> model : modelList) {
            if (updateObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        // MetaModel not found (not an Annotated Class). . .
        if (correctModel == null) {
            // throw exception...
        }
        // Update the object. . .
        return SQLService.update(correctModel, updateObj, oldObj);
    }

    /**
     * Provides all of the objects (records) in the database of a certain class / model
     * @param clazz the class to look for
     * @return a List of all of the objects / records found
     */
    public List<?> getAll(Class<?> clazz) {

        // MetaModel for the class. . .
        MetaModel<?> correctModel = null;

        // Find the correct model. . .
        for (MetaModel<?> model : modelList) {
            if (clazz.getName().equals(model.getClassName()))
                correctModel = model;
        }

        // Get a List of all of the objects corresponding to the class's MetaModel. . .
        return SQLService.getAll(correctModel);
    }
}
