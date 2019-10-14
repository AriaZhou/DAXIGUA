package com.example.demo.dao;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderDAO extends CrudRepository<Orders, String> {

    Iterable<Orders> findByProductId(Product product);

    @Query("select o from Orders o where o.user.username = :username order by o.state.id")
    Iterable<Orders> findbyidAndOrderByState(@Param("username") String username);

    @Query("select o from Orders o order by o.state")
    Iterable<Orders> findALLByState();
//	Orders findById(String id);
//	List<Orders> findAll();
//	int insert(Orders o);
//	@Query("select o from Orders o where o.user = userName")
//	public Iterable<Orders> findByUsr(String userName);
//	int deleteById(String id);
//	int modifyOrder(Orders o);
	
}