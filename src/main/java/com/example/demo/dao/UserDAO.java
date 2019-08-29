package com.example.demo.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserDAO extends CrudRepository<User, String>{

//	User checkPsw(String phone, String psw);
//	User findById(String id);
//	int insert(User u);
//	int checkRole(String u);
//	int modifyUser(User u);
	
}