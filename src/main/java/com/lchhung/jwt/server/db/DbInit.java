package com.lchhung.jwt.server.db;

import com.lchhung.jwt.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {

        this.repository.deleteAll();

        User user = new User("user", passwordEncoder.encode("user"),"","");
        User admin = new User("admin",passwordEncoder.encode("admin"),"ADMIN","ACCESS_TEST1,ACCESS_TEST2");
        User manager = new User("manager",passwordEncoder.encode("manager"),"MANAGER","ACCESS_TEST1");

        //List<User> userList = Arrays.asList(user, admin, manager);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(admin);
        userList.add(manager);


        this.repository.saveAll(userList);

    }




}
