package com.revature.util;

import com.revature.repos.DMLRepo;
import com.revature.services.DMLService;

import java.util.LinkedList;
import java.util.List;

public class SessionFactory {

    private static SessionFactory sessFactory = new SessionFactory();

    private List<MetaModel<Class<?>>> modelList;

    private DMLService dmlService;

    public SessionFactory() {
        modelList = new LinkedList<>();

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

    public void delete (Object deleteObj) {
        dmlService.delete(deleteObj);
    }
}
