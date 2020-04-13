package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select * from product p where p.groupid = :groupid and p.pname = :pname for update", nativeQuery = true)
    Product findByNameAndGroup(@Param("pname") String pname, @Param("groupid") String groupid);

    @Query("select p from Product p where p.group.id = :groupid AND p.state.id = 0 AND p.pcount > 0")
    Iterable<Product> findByGroupIdVali(
            @Param("groupid") String groupid);

    @Query("select p from Product p where concat(NULLIF(p.group.id, ''), NULLIF(p.pname, ''), NULLIF(p.state.value, '')) LIKE :param order by p.state.id")
    Page<Product> findALLByStateAndParam(@Param("param") String param, Pageable pageable);

    @Query("select p from Product p order by p.state.id ")
    Page<Product> findALLByState(Pageable pageable);

//	Product findById(String id);
//	List<Product> findAll();
//	int insert(Product p);
//	int deleteById(String id);
//	int modifyProduct(Product p);
//	List<Product> findByTime();
	
}