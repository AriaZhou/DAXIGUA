package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "porder")
public class Orders implements Serializable {

    private static final long serialVersionUID = 3316076651716569539L;
    @Id
    private String id;
    private int ocount;
    private int state;
    private String time;
    private String price;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getOcount() {
        return ocount;
    }
    public void setOcount(int ocount) {
        this.ocount = ocount;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
