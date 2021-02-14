package com.revature;

import com.revature.testclass.User;
import com.revature.util.Session;
import com.revature.util.SessionFactory;

public class GauntletDriver {

    public static void main(String[] args) {
        User user = new User("alex_googe57","password", "Alex","Googe");
        SessionFactory manager = new SessionFactory();
        manager.addAnnotatedClass(User.class);

        Session session = manager.openSession();
        session.save(user);

        System.out.println("User was inserted...");

    }

}
