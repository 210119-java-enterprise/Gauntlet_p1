package com.revature.services;

import com.revature.repos.DMLRepo;
import com.revature.util.MetaModel;

public class DMLService {

    public DMLRepo dmlRepo;

    public DMLService (DMLRepo dmlRepo) {
        this.dmlRepo = dmlRepo;
    }

    public void insert(MetaModel<?> model, Object obj) {

        if (obj == null) {
            // throw error...
        }
        dmlRepo.insert(model, obj);

    }

    public void delete() {
        // TODO
    }

    public void update() {
        // TODO
    }

}
