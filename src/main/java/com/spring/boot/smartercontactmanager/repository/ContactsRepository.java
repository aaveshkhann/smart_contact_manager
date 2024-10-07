package com.spring.boot.smartercontactmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.boot.smartercontactmanager.entites.Contact;
import java.util.*;
public interface ContactsRepository extends JpaRepository<Contact, Integer> {

    // Custom query using @Query
    @Query("from Contact c where c.user.id = :userId")
    Page<Contact> getDataFromUserId(@Param("userId") int userId, Pageable pageable);
    
    @Query("SELECT c FROM Contact c WHERE CONCAT(c.cName, ' ', c.email, ' ', c.work) LIKE %:keyword%")
    List<Contact> searchDataByName(@Param("keyword") String keyword);
    
}
