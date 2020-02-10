package com.example.demo.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	//payment state
	public static final long PASTATE_UNPAID = 0;//未支付
	public static final long PASTATE_PAYFAIL = 1;//支付失败，金额有误
	public static final long PASTATE_PAID = 2;//已支付
	public static final long PASTATE_DANDAN = 3;//已支付可在淡淡处清货
	public static final long PASTATE_GUIYUAN = 4;//已支付可在桂圆处清货
	public static final long PASTATE_DONE = 5;//出货完成

	//payment type
	public static final long PASTYPE_PRODUCT = 0;//货物支付订单
	public static final long PASTYLE_FREGHT = 1;//运费支付订单

	private static final long serialVersionUID = -2957645392914180170L;
	@Id
	private String id;
	private String value;
	private double totprice;
	private double actualprice;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date time;

	@ManyToOne
	@JoinColumn(name = "username")
	private User user;

	@ManyToOne
	@JoinColumn(name = "state")
	private PaState state;

	@ManyToOne
	@JoinColumn(name = "type")
	private PType type;

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

	public PType getType() {
		return type;
	}

	public void setType(PType type) {
		this.type = type;
	}

	public PaState getState() {
		return state;
	}

	public void setState(PaState state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public double getActualprice() {
		return actualprice;
	}

	public void setActualprice(double actualprice) {
		this.actualprice = actualprice;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
