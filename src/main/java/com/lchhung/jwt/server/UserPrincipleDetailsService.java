package com.lchhung.jwt.server;

import com.lchhung.jwt.server.db.UserRepository;
import com.lchhung.jwt.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserPrincipleDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository repository;


    // This method is to receive
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        //1. Extract user from database
        User user = this.repository.findByUsername(s);

        //2. Convert this extracted user into User principle

        UserPrinciple userPrinciple = new UserPrinciple(user);

        return userPrinciple;
    }


}
