package com.spring.boot.smartercontactmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.boot.smartercontactmanager.entites.User;

public interface UserRepository extends CrudRepository<User, Integer> {
@Query("select u from User u where u.email =:email")
public User getUserByEmail(@Param("email") String email);

@Query("select u from User u where u.name =:name")
public User getUserByName(@RequestParam("name") String name);
    
} 