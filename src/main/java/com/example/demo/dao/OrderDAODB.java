package com.example.demo.dao;

import com.example.demo.entity.Orders;
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
public class OrderDAODB{

	//@Autowired
	//private DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final class OrderMapper implements RowMapper<List<Orders>>{

		UserDAO userDAO;
		ProductDAO productDAO;

		public List<Orders> mapRow(ResultSet rs, int rowNum) throws SQLException {

			List<Orders> orderLst = new ArrayList<>();
			for(int i= 0; i < rs.getRow(); i++){
				Orders order = new Orders();
				order.setId(rs.getString("id"));
//				order.setUser(userDAO.findById(rs.getString("username")));
				order.setProduct(productDAO.findById(rs.getString("productId")).get());
				order.setOcount(rs.getInt("ocount"));
				order.setState(rs.getInt("state"));
				order.setTime(rs.getString("time"));
				order.setPrice(rs.getString("price"));

				rs.next();
				orderLst.add(order);
			}

			return orderLst;
	     }
		
	}

	public List<Orders> findAll() {
		List<Orders> o;
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


	public Orders findById(String id) {
		Orders o;
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

	public List<Orders> findByUsr(String userName) {
		List<Orders> o;
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

	public int insert(Orders o){
		try{
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			jdbcTemplate.update("insert into porder (id, username, productid, ocount, state, time, price)" +
					"values (?,?,?,?,?,?,?)", o.getUser().getUsername()+now.getTime(), o.getUser().getUsername(), o.getProduct().getId(), o.getOcount(), 0, format.format(now), o.getPrice());
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

	public int modifyOrder(Orders o){

		try{
			jdbcTemplate.update("update porder set id=?, username=?, productid=?, ocount=?, state=?, price=? " +
					"where id=?", o.getId(), o.getUser().getUsername(), o.getProduct().getId(), o.getOcount(), o.getState(), o.getPrice(), o.getId());
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

}
