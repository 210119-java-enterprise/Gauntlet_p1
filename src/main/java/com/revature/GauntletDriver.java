package com.revature;

import com.revature.testclass.User;
import com.revature.util.Manager;

public class GauntletDriver {

    public static void main(String[] args) {
        User user = new User("alex_googe","password", "Alex","Googe");
        Manager manager = new Manager();
        manager.addAnnotatedClass(User.class);

        manager.save(user);
    }

}
