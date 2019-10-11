package com.example.demo.dao;

import com.example.demo.entity.Payment;
import com.example.demo.entity.State;
import org.springframework.data.repository.CrudRepository;

public interface PaymentDAO extends CrudRepository<Payment, String> {
	
}