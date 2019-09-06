package com.example.demo.dao;

import com.example.demo.entity.Group;
import com.example.demo.entity.State;
import org.springframework.data.repository.CrudRepository;

public interface StateDAO extends CrudRepository<State, Long> {
	
}