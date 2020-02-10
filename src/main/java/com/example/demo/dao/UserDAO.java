package com.example.demo.dao;

import com.example.demo.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends CrudRepository<User, String>{

    @Query("select u from User u where concat(NULLIF(u.username, ''), NULLIF(u.uname, ''), NULLIF(u.phone, '')) LIKE :param order by u.username desc")
    Page<User> findALLByStateAndParam(@Param("param") String param, Pageable pageable);

    @Query("select u from User u order by u.username desc")
    Page<User> findALLByState(Pageable pageable);

//	User checkPsw(String phone, String psw);
//	User findById(String id);
//	int insert(User u);
//	int checkRole(String u);
//	int modifyUser(User u);
	
}