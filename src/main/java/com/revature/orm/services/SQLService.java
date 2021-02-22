package com.revature.orm.services;

import com.revature.orm.repos.DMLRepo;
import com.revature.orm.repos.DQLRepo;
import com.revature.orm.util.ColumnField;
import com.revature.orm.util.MetaModel;

import java.util.List;

/**
 * Service class for handling any basic SQL Queries
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class SQLService {

    /**
     * DMLRepo that will handle the creation and execution of JDBC DML queries
     */
    private DMLRepo dmlRepo;

    /**
     * DQLRepo that will handle the creation and execution of JDBC DQL queries
     */
    private DQLRepo dqlRepo;


    /**
     * SQLService constructor that takes in the Repos needed
     * @param dmlRepo the Repo that handles DML queries
     * @param dqlRepo the Repo that handles DQL queries
     */
    public SQLService(DMLRepo dmlRepo, DQLRepo dqlRepo) {
        this.dmlRepo = dmlRepo;
        this.dqlRepo = dqlRepo;
    }

    /**
     * Inserts a new Object into the database based off of the MetaModel provided
     * @param model the MetaModel corresponding to the newObj
     * @param newObj the Object to be inserted into the database
     * @return true if the Object was inserted, false if it was not
     */
    public int insert(MetaModel<?> model, Object newObj) {

        if (newObj == null) {
            // throw error...
        }
        return dmlRepo.insert(model, newObj);

    }

    /**
     * Deletes a deleteObj from the database
     * @param deleteObj the Object to be deleted
     * @return true if the Object was deleted, false if it was not deleted
     */
    public boolean delete(Object deleteObj) {

        if (deleteObj == null) {
            // throw error...
        }

        return dmlRepo.delete(deleteObj);
    }

    /**
     * Updates the oldObj record to the updateObj's data
     * @param model the MetaModel corresponding to updateObj and oldObj
     * @param updateObj the Object that will provide the new values for the record
     * @param oldObj the Object that will be updated
     * @return true if the Object was updated, false if there was no update
     */
    public boolean update(MetaModel<?> model, Object updateObj, Object oldObj) {

        if (updateObj == null && oldObj == null) {
            // throw exception
        }

        return dmlRepo.update(model, updateObj, oldObj);
    }

    /**
     * Selects all Objects of a MetaModel
     * @param model the MetaModel that the user wishes to grab the Objects of
     * @return a List of all the Objects that the MetaModel corresponds to
     */
    public List<?> getAll(MetaModel<?> model) {
        return dqlRepo.selectAll(model);
    }
}
