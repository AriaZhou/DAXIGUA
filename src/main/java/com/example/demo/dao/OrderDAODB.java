package com.example.demo.dao;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDAODB implements OrderDAO{

	//@Autowired
	//private DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final class OrderMapper implements RowMapper<List<Order>>{

		public List<Order> mapRow(ResultSet rs, int rowNum) throws SQLException {

			List<Order> orderLst = new ArrayList<>();
			for(int i= 0; i < rs.getRow(); i++){
				Order order = new Order();
				order.setId(rs.getString("id"));
				order.setUsername(rs.getString("username"));
				order.setProductId(rs.getString("productId"));
				order.setCount(rs.getInt("ocount"));
				order.setState(rs.getInt("state"));
				order.setTime(rs.getString("time"));
				order.setPrice(rs.getString("price"));

				rs.next();
				orderLst.add(order);
			}

			return orderLst;
	     }
		
	}

	public List<Order> findAll() {
		List<Order> o;
		try {
			o = jdbcTemplate.queryForObject("select id, username, productId, ocount, price, state, time"
					+ " from porder ", new OrderMapper());
		}catch(Exception e) {
			o = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return o;
	}


	public Order findById(String id) {
		Order o;
		try {
			o = jdbcTemplate.queryForObject("select id, username, productId, ocount, price, state, time"
					+ " from porder where id = ?", new Object[]{id}, new OrderMapper()).get(0);
		}catch(Exception e) {
			o = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return o;
	}

	public List<Order> findByUsr(String userName) {
		List<Order> o;
		try {
			o = jdbcTemplate.queryForObject("select id, username, productId, ocount, price, state, time"
					+ " from porder where username = ?", new Object[]{userName}, new OrderMapper());
		}catch(Exception e) {
			o = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return o;
	}

	public int insert(Order o){
		try{
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			jdbcTemplate.update("insert into porder (id, username, productid, ocount, state, time, price)" +
					"values (?,?,?,?,?,?,?)", o.getUsername()+now.getTime(), o.getUsername(), o.getProductId(), o.getCount(), 0, format.format(now), o.getPrice());
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

	public int deleteById(String id){
		try{
			jdbcTemplate.update("delete from porder where id = ?", id);
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

	public int modifyOrder(Order o){

		try{
			jdbcTemplate.update("update porder set id=?, username=?, productid=?, ocount=?, state=?, price=? " +
					"where id=?", o.getId(), o.getUsername(), o.getProductId(), o.getCount(), o.getState(), o.getPrice(), o.getId());
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

}
