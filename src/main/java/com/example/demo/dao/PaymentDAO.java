package com.example.demo.dao;

import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentDAO extends CrudRepository<Payment, String> {

    @Query("select p from Payment p where p.value like :oid")
    Iterable<Payment> findbyOrderid(@Param("oid") String oid);

    @Query("select p from Payment p where p.user.username = :username order by p.state")
    Iterable<Payment> findbyidAndOrderByState(@Param("username") String username);

    @Query("select p from Payment p order by p.state")
    Iterable<Payment> findALLByState();
}