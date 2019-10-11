package com.example.demo.entity;

import com.example.demo.dao.ProductDAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pstate")

public class PState implements Serializable {

    private static final long serialVersionUID = -2957645392914180170L;
    @Id
    private Long id;
    private String value;

    @OneToMany(mappedBy = "state")
    private List<Product> products;

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
    public Iterable<Product> getProduct() {
        return products;
    }
}
