package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ostate")
public class State implements Serializable {

	private static final long serialVersionUID = -2957645392914180170L;
	@Id
	private Long id;
	private String value;

	@OneToMany(mappedBy = "state")
	private List<Orders> orders;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Iterable<Orders> getOrders() {
		return orders;
	}
}
