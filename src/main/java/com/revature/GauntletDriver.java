package com.revature;

import com.revature.testclass.User;
import com.revature.orm.util.Configuration;
import com.revature.orm.util.Session;
import com.revature.orm.util.SessionFactory;

import java.util.List;

public class GauntletDriver {

    public static void main(String[] args) {
        User user = new User("alex_googe5750000006","password", "Alex","Googe");
        //User user1 = new User("alex_googe_updated", "password_updated", "Alex", "Googe");

        //User user1 = new User(1);
        SessionFactory factory = new Configuration("src/main/resources/app.properties")
                                .addAnnotatedClass(User.class)
                                .buildSessionFactory();

        Session session = factory.openSession();
        int newId = session.save(user);

        System.out.println(newId);

        //if(session.update(user1, user))
          //  System.out.println("success!");

        //List<User> users = (List<User>) session.getAll(User.class);

        /*for (User u : users) {
            System.out.println(u.toString());
        }*/

    }

}
