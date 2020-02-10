package com.example.demo.dao;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentDAO extends CrudRepository<Payment, String> {

    @Query("select p from Payment p where p.value like :oid")
    Iterable<Payment> findbyOrderid(@Param("oid") String oid);

    @Query("select p from Payment p where p.user.username = :username order by p.state.id")
    Iterable<Payment> findbyidAndOrderByState(@Param("username") String username);

    @Query("select p from Payment p where concat(NULLIF(p.id, ''), NULLIF(p.value, ''), NULLIF(p.state.value, ''), " +
            "NULLIF(p.user.username, ''), NULLIF(p.user.uname, '')) LIKE :param order by p.state.id")
    Page<Payment> findALLByStateAndParam(@Param("param") String param, Pageable pageable);

    @Query("select p from Payment p order by p.state.id")
    Page<Payment> findALLByState(Pageable pageable);

    @Query("select p from Payment p where p.state.id = 3 or p.state.id = 4 order by p.state.id")
    Iterable<Payment> findALLReadyShip();
}