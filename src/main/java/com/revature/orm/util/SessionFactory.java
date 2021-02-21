package com.revature.orm.util;

import com.revature.orm.repos.DMLRepo;
import com.revature.orm.services.DMLService;

import java.util.List;

public class SessionFactory {

    private List<MetaModel<Class<?>>> modelList;

    private DMLService dmlService;

    public SessionFactory(List<MetaModel<Class<?>>> modelList) {
        this.modelList = modelList;

        final DMLRepo dmlRepo = new DMLRepo();

        dmlService = new DMLService(dmlRepo);
    }

    @SuppressWarnings({"unchecked"})
    public SessionFactory addAnnotatedClass(Class clazz) {
        modelList.add(MetaModel.of(clazz));
        return this;
    }

    // TODO: Connection Pooling instead of grabbing from ConnectionFactory.
    public Session openSession () {
        Session session = new Session(modelList, dmlService, ConnectionFactory.getInstance().getConnection());
        return session;

    }
}
