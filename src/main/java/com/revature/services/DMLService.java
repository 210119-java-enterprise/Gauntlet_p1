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

    public void delete(Object deleteObj) {

        if (deleteObj == null) {
            // throw error...
        }

        dmlRepo.delete(deleteObj);
    }

    public void update() {
        // TODO
    }

}
