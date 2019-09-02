package com.example.demo.dao;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;

import java.util.List;

public interface ProductDAO {

	Product findById(String id);
	List<Product> findAll();
	int insert(Product p);
	int deleteById(String id);
	int modifyProduct(Product p);
	List<Product> findByTime();
	
}