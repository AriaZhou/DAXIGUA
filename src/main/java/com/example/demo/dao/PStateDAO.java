package com.example.demo.dao;

import com.example.demo.entity.PState;
import com.example.demo.entity.State;
import org.springframework.data.repository.CrudRepository;

public interface PStateDAO extends CrudRepository<PState, Long> {
	
}