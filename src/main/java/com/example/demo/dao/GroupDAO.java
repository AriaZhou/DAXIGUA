package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface GroupDAO extends CrudRepository<Group, String> {

    @Query("select g from Group g where g.starttime <= :nowTime AND g.endtime >= :nowTime")
    Iterable<Group> findAllWithNowTimeBefore(
            @Param("nowTime") Date nowTime);

    @Query("select g from Group g where g.endtime < :nowTime")
    Iterable<Group> findAllWithNowTimeAfter(
            @Param("nowTime") Date nowTime);
	
}