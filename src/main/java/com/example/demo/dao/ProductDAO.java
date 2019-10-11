package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductDAO extends CrudRepository<Product, String> {

    Iterable<Product> findByGroupId(String group);

    @Query("select p from Product p where p.group.id = :groupid AND p.state.id = 0")
    Iterable<Product> fingbygroupinsatte0(
            @Param("groupid") String groupid);

    @Query(value = "select * from Product p where p.id = :id for update", nativeQuery = true)
    Product findProductForUpdate(@Param("id") String pid);

//	Product findById(String id);
//	List<Product> findAll();
//	int insert(Product p);
//	int deleteById(String id);
//	int modifyProduct(Product p);
//	List<Product> findByTime();
	
}