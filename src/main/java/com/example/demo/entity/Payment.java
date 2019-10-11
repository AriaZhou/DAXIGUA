package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	private static final long serialVersionUID = -2957645392914180170L;
	@Id
	private String id;
	private String value;
	private double totprice;
	private int state;

	@ManyToOne
	@JoinColumn(name = "username")
	private User user;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public double getTotprice() {
		return totprice;
	}
	public void setTotprice(double totprice) {
		this.totprice = totprice;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
