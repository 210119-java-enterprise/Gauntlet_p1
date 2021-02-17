package com.revature;

import com.revature.testclass.User;
import com.revature.util.Configuration;
import com.revature.util.Session;
import com.revature.util.SessionFactory;

import java.util.List;

public class GauntletDriver {

    public static void main(String[] args) {
        //User user = new User("alex_googe1337","password", "Alex","Googe");
        //User user1 = new User("alex_googe_updated", "password_updated", "Alex", "Googe");

        //User user1 = new User(1);
        SessionFactory factory = new Configuration()
                                .addAnnotatedClass(User.class)
                                .buildSessionFactory();

        Session session = factory.openSession();
        //session.save(user);

        //if(session.update(user1, user))
          //  System.out.println("success!");

        List<User> users = (List<User>) session.getAll(User.class);

        for (User u : users) {
            System.out.println(u.toString());
        }

    }

}
