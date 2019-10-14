package com.example.demo.dao;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductDAO extends CrudRepository<Product, String> {

    Iterable<Product> findByGroupId(String group);

    @Query("select p from Product p where p.group.id = :groupid AND p.state.id = 0")
    Iterable<Product> fingbygroupinsatte0(
            @Param("groupid") String groupid);

    @Query(value = "select * from product p where p.id = :id for update", nativeQuery = true)
    Product findProductForUpdate(@Param("id") String pid);

    @Query("select p from Product p where p.group.id = :groupid AND p.state.id = 0 AND p.pcount > 0")
    Iterable<Product> findByGroupIdVali(
            @Param("groupid") String groupid);

    @Query("select p from Product p order by p.state")
    Iterable<Product> findALLByState();

//	Product findById(String id);
//	List<Product> findAll();
//	int insert(Product p);
//	int deleteById(String id);
//	int modifyProduct(Product p);
//	List<Product> findByTime();
	
}