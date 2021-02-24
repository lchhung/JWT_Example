package com.lchhung.jwt.server.db;

import com.lchhung.jwt.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername (String username);

    List<User> findAll();

}
