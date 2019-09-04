package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDAO extends CrudRepository<Product, String> {

    Iterable<Product> findByGroupId(String group);
//	Product findById(String id);
//	List<Product> findAll();
//	int insert(Product p);
//	int deleteById(String id);
//	int modifyProduct(Product p);
//	List<Product> findByTime();
	
}