package com.revature;

import com.revature.testclass.User;
import com.revature.util.Configuration;
import com.revature.util.Session;
import com.revature.util.SessionFactory;

public class GauntletDriver {

    public static void main(String[] args) {
        User user = new User("alex_googe57","password", "Alex","Googe");
        SessionFactory factory = new Configuration()
                                .addAnnotatedClass(User.class)
                                .buildSessionFactory();

        Session session = factory.openSession();
        session.save(user);

        System.out.println("User was inserted...");

    }

}
