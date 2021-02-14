package com.revature.services;

import com.revature.repos.DMLRepo;
import com.revature.util.MetaModel;

public class DMLService {

    public DMLRepo dmlRepo;

    public DMLService (DMLRepo dmlRepo) {
        this.dmlRepo = dmlRepo;
    }

    public boolean insert(MetaModel<?> model, Object newObj) {

        if (newObj == null) {
            // throw error...
        }
        return dmlRepo.insert(model, newObj);

    }

    public boolean delete(Object deleteObj) {

        if (deleteObj == null) {
            // throw error...
        }

        return dmlRepo.delete(deleteObj);
    }

    public boolean update(MetaModel<?> model, Object updateObj, Object oldObj) {

        if (updateObj == null && oldObj == null) {
            // throw exception
        }

        return dmlRepo.update(model, updateObj, oldObj);
    }

}
