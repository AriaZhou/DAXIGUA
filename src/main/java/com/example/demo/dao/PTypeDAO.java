package com.example.demo.dao;

import com.example.demo.entity.PType;
import com.example.demo.entity.PaState;
import org.springframework.data.repository.CrudRepository;

public interface PTypeDAO extends CrudRepository<PType, Long> {
	
}