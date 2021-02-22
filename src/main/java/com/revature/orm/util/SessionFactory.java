package com.revature.orm.util;

import com.revature.orm.repos.DMLRepo;
import com.revature.orm.services.SQLService;

import java.util.List;

public class SessionFactory {

    private List<MetaModel<Class<?>>> modelList;

    private SQLService SQLService;

    public SessionFactory(List<MetaModel<Class<?>>> modelList) {
        this.modelList = modelList;

        final DMLRepo dmlRepo = new DMLRepo();

        SQLService = new SQLService(dmlRepo);
    }

    @SuppressWarnings({"unchecked"})
    public SessionFactory addAnnotatedClass(Class clazz) {
        modelList.add(MetaModel.of(clazz));
        return this;
    }

    // TODO: Connection Pooling instead of grabbing from ConnectionFactory.
    public Session openSession () {
        Session session = new Session(modelList, SQLService, ConnectionFactory.getInstance().getConnection());
        return session;

    }
}
