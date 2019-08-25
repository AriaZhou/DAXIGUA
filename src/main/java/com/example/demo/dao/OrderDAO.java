package com.example.demo.dao;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;

import java.util.List;

public interface OrderDAO {

	Order findById(String id);
	List<Order> findAll();
	int insert(Order o);
	List<Order> findByUsr(String userName);
	int deleteById(String id);
	
}