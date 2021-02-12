package com.revature.util;

import com.revature.repos.DMLRepo;
import com.revature.services.DMLService;

import java.util.LinkedList;
import java.util.List;

public class Manager {

    private List<MetaModel<Class<?>>> modelList;

    private DMLService dmlService;

    public Manager() {
        modelList = new LinkedList<>();

        final DMLRepo dmlRepo = new DMLRepo();

        dmlService = new DMLService(dmlRepo);
    }

    @SuppressWarnings({"unchecked"})
    public Manager addAnnotatedClass(Class clazz) {
        modelList.add(MetaModel.of(clazz));
        return this;
    }

    public void save (Object newObj) {

        MetaModel<?> correctModel = null;

        for (MetaModel<?> model : modelList) {
            if (newObj.getClass().getName().equals(model.getClassName())) {
                correctModel = model;
            }
        }

        if (correctModel == null) {
            // throw exception...
        }

        dmlService.insert(correctModel, newObj);

    }
}
