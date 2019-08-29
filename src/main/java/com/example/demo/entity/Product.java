package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
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
    private String starttime;
    private String endtime;
    private int enabled;

    @OneToMany(mappedBy = "product")
    private List<Orders> orders;

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
    public String getName() {
        return pname;
    }
    public void setName(String pname) {
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
    public int getCount() {
        return pcount;
    }
    public void setCount(int pcount) {
        this.pcount = pcount;
    }
    public String getStartTime() {
        return starttime;
    }
    public void setStartTime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndTime() {
        return endtime;
    }
    public void setEndTime(String endtime) {
        this.endtime = endtime;
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
}
