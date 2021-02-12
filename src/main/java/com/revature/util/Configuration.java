package com.revature.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private List<MetaModel<Class<?>>> metaModels;
    private Properties props = new Properties();

    @SuppressWarnings({"unchecked"})
    public Configuration addAnnotatedClass (Class annotatedClass) {

        if (metaModels == null) {
            metaModels = new LinkedList<>();
        }

        metaModels.add(MetaModel.of(annotatedClass));

        return this;
    }

    public List<MetaModel<Class<?>>> getMetaModels() {
        return (metaModels == null) ? Collections.emptyList() : metaModels;
    }

    public SessionFactory buildSessionFactory() {
        try {
            props.load(new FileReader("src/main/resources/app.properties"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return new SessionFactory(
                props.getProperty("url"),
                props.getProperty("admin-usr"),
                props.getProperty("admin-pw"));
    }

}
