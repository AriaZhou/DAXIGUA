package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "porder")
public class Orders implements Serializable {

    //order state
    public static final long OSTATE_UNPAID = 0;//未支付
    public static final long OSTATE_SUBMIT_PRODUCT_PAID = 1;//提交支付货款
    public static final long OSTATE_PRODUCT_PAID = 2;//已支付货款
    public static final long OSTATE_SUBMIT_FERIGHT_PAID = 3;//提交支付运费
    public static final long OSTATE_FERIGHT_PAID = 4;//已支付运费
    public static final long OSTATE_SHIPMENT = 5;//已清货
    public static final long OSTATE_SUBMIT_REFUND = 6;//申请退款
    public static final long OSTATE_REFUND = 7;//已退款

    private static final long serialVersionUID = 3316076651716569539L;
    @Id
    private String id;
    private int ocount;
    private String time;
    private String price;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ostate")
    private State state;

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
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
}
