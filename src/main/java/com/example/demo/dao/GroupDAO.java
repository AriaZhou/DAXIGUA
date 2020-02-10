package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface GroupDAO extends CrudRepository<Group, String> {

    //所有还没有截团的团
    @Query("select g from Group g where g.starttime <= :nowTime AND g.endtime >= :nowTime")
    Iterable<Group> findAllWithNowTimeBefore(
            @Param("nowTime") Date nowTime);

    //所有已经截团了的团
    @Query("select g from Group g where g.endtime < :nowTime")
    Iterable<Group> findAllWithNowTimeAfter(
            @Param("nowTime") Date nowTime);

    @Query("select g from Group g where g.id LIKE :param order by g.endtime desc")
    Page<Group> findALLByStateAndParam(@Param("param") String param, Pageable pageable);

    @Query("select g from Group g order by g.endtime desc ")
    Page<Group> findALLByState(Pageable pageable);
	
}