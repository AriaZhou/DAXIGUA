package com.example.demo.dao;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface OrderDAO extends CrudRepository<Orders, String> {

    Iterable<Orders> findByProductId(Product product);
//	Orders findById(String id);
//	List<Orders> findAll();
//	int insert(Orders o);
//	@Query("select o from Orders o where o.user = userName")
//	public Iterable<Orders> findByUsr(String userName);
//	int deleteById(String id);
//	int modifyOrder(Orders o);
	
}