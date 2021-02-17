package com.revature.services;

import com.revature.repos.DMLRepo;
import com.revature.util.MetaModel;

import java.util.List;

/**
 * Service class for handling any JDBC Data Manipulation Language Queries
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class DMLService {

    /**
     * DMLRepo that will handle the creation and execution of JDBC DML queries
     */
    private DMLRepo dmlRepo;

    /**
     * Constructor for DMLService
     * @param dmlRepo the DMLRepo that will be handling creation and execution of JDBC DML queries
     */
    public DMLService (DMLRepo dmlRepo) {
        this.dmlRepo = dmlRepo;
    }

    /**
     * Inserts a new Object into the database based off of the MetaModel provided
     * @param model the MetaModel corresponding to the newObj
     * @param newObj the Object to be inserted into the database
     * @return true if the Object was inserted, false if it was not
     */
    public boolean insert(MetaModel<?> model, Object newObj) {

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
        return dmlRepo.selectAll(model);
    }

}
