package com.example.demo.dao;

import com.example.demo.entity.PState;
import com.example.demo.entity.PaState;
import org.springframework.data.repository.CrudRepository;

public interface PaStateDAO extends CrudRepository<PaState, Long> {
	
}