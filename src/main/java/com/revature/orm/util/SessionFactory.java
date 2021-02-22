package com.revature.orm.util;

import com.revature.orm.repos.DMLRepo;
import com.revature.orm.repos.DQLRepo;
import com.revature.orm.services.SQLService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates Sessions that will handle simple JDBC
 *
 * @author Alex Googe (github: darkspearrai)
 */
public class SessionFactory {

    /**
     * The List of MetaModels for Annotated Classes provided by the User
     */
    private List<MetaModel<Class<?>>> modelList;

    /**
     * Service class that handles SQL
     */
    private SQLService SQLService;

    /**
     * Constructor for the SessionFactory
     * @param modelList the MetaModel List passed in by the Configuration class
     */
    public SessionFactory(List<MetaModel<Class<?>>> modelList) {
        this.modelList = modelList;

        final DMLRepo dmlRepo = new DMLRepo();
        final DQLRepo dqlRepo = new DQLRepo();

        SQLService = new SQLService(dmlRepo, dqlRepo);
    }

    /**
     * Adds a class that has been fully annotated to the MetaModel list
     * @param clazz the annotated class
     * @return a SessionFactory so it can be chained
     */
    @SuppressWarnings({"unchecked"})
    public SessionFactory addAnnotatedClass(Class clazz) {
        modelList.add(MetaModel.of(clazz));
        return this;
    }

    /**
     * Creates and returns a Session Object
     * @return the Session that was created
     */
    // TODO: Connection Pooling instead of grabbing from ConnectionFactory.
    public Session openSession () {
        Session session = new Session(modelList, SQLService, ConnectionFactory.getInstance().getConnection());
        return session;

    }
}
