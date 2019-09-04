package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pgroup")
public class Group implements Serializable {

    private static final long serialVersionUID = -2957645392914180170L;

    @Id
    private String id;
    private String starttime;
    private String endtime;

    @OneToMany(mappedBy = "group")
    private List<Product> products;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public Iterable<Product> getProducts() {
        return products;
    }
}
