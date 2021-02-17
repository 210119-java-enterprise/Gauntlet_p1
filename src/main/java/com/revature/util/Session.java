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
    public Object get (Class<?> clazz, int id) {

    }
    */


    public boolean delete (Object deleteObj) {
        return dmlService.delete(deleteObj);

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
        return dmlService.update(correctModel, updateObj, oldObj);
    }

    public List<?> getAll(Class<?> clazz) {

        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (clazz.getName().equals(model.getClassName()))
                correctModel = model;
        }

        return dmlService.getAll(correctModel);
    }
}
