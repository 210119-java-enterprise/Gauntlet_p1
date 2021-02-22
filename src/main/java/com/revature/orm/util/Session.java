package com.revature.orm.util;

import com.revature.orm.services.SQLService;

import java.sql.Connection;
import java.util.List;

public class Session {

    private List<MetaModel<Class<?>>> modelList;
    private SQLService SQLService;
    private Connection conn;

    public Session (List<MetaModel<Class<?>>> modelList, SQLService SQLService, Connection conn) {
        this.modelList = modelList;
        this.SQLService = SQLService;
        this.conn = conn;
    }

    public int save (Object newObj) {

        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (newObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        if (correctModel == null) {
            // throw exception...
        }

        return SQLService.insert(correctModel, newObj);


    }

    /*
    public Object get (Class<?> clazz, int id) {

    }
    */


    public boolean delete (Object deleteObj) {
        return SQLService.delete(deleteObj);

    }

    public boolean update(Object updateObj, Object oldObj) {

        if(!updateObj.getClass().equals(oldObj.getClass())) {
            // throw exception...
        }
        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (updateObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        if (correctModel == null) {
            // throw exception...
        }
        return SQLService.update(correctModel, updateObj, oldObj);
    }

    public List<?> getAll(Class<?> clazz) {

        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (clazz.getName().equals(model.getClassName()))
                correctModel = model;
        }

        return SQLService.getAll(correctModel);
    }
}
