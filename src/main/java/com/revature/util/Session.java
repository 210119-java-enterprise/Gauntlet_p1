package com.revature.util;

import com.revature.services.DMLService;

import java.sql.Connection;
import java.util.List;

public class Session {

    private List<MetaModel<Class<?>>> modelList;
    private DMLService dmlService;
    private Connection conn;

    public Session (List<MetaModel<Class<?>>> modelList, DMLService dmlService, Connection conn) {
        this.modelList = modelList;
        this.dmlService = dmlService;
        this.conn = conn;
    }

    public boolean save (Object newObj) {

        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (newObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        if (correctModel == null) {
            // throw exception...
        }

        return dmlService.insert(correctModel, newObj);


    }

    /*
    public Object get() {
      TODO
    }
    */
    public boolean delete() {
        return false;
    }

    public boolean update() {
        return false;
    }
}
