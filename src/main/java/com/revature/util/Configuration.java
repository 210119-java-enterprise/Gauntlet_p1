package com.revature.util;

import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private List<MetaModel<Class<?>>> metaModels;
    private Properties props = new Properties();
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public Configuration() {
        try {
            props.load(new FileReader("src/main/resources/app.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbUrl = props.getProperty("url");
        dbUser = props.getProperty("admin-usr");
        dbPass = props.getProperty("admin-pw");
    }

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

}
