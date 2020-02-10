package com.example.demo.dao;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderDAO extends CrudRepository<Orders, String> {

    Iterable<Orders> findByProductId(Product product);

    @Query("select o from Orders o where o.user.username = :username order by o.state.id")
    Iterable<Orders> findbyidAndOrderByState(@Param("username") String username);

    @Query("select o from Orders o where concat(NULLIF(o.id, ''), NULLIF(o.product.group.id, ''), NULLIF(o.product.pname, ''), " +
            "NULLIF(o.state.value, ''), NULLIF(o.user.username, ''), NULLIF(o.user.uname, '')) LIKE :param order by o.state.id")
    Page<Orders> findALLByStateAndParam(@Param("param") String param, Pageable pageable);

    @Query("select o from Orders o order by o.state.id")
    Page<Orders> findALLByState(Pageable pageable);
	
}