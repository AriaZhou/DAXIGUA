package com.example.demo.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserDAO{

	public User checkPsw(String phone, String psw);
	public User findById(String id);
	public int insert(User u);
	
}