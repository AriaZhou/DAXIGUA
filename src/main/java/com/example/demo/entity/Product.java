package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -2957645392914180170L;

    @Id
    private String id;
    private String username;
    private String uploadtime;
    private String pname;
    private String price;
    private String description;
    private int pcount;
    private int enabled;
    private int ordercount;

    @OneToMany(mappedBy = "product")
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name = "groupid")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "pstate")
    private PState state;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUploadTime() {
        return uploadtime;
    }
    public void setUploadTime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPcount() {
        return pcount;
    }
    public void setPcount(int pcount) {
        this.pcount = pcount;
    }
    public int getEnabled() {
        return enabled;
    }
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
    public Iterable<Orders> getOrders() {
        return orders;
    }
    public Group getGroup() {
        return group;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public PState getState() {
        return state;
    }
    public void setState(PState pstate) {
        this.state = pstate;
    }
    public String getUploadtime() {
        return uploadtime;
    }
    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
    public int getOrdercount() {
        return ordercount;
    }
    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }
}
