package com.example.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public class UserDAODB {

	//@Autowired
	//private DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final class UserMapper implements RowMapper<List<User>>{
		
		public List<User> mapRow(ResultSet rs, int rowNum) throws SQLException {
			List<User> uList = new ArrayList<>();
			for(int i= 0; i < rs.getRow(); i++){
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setUname(rs.getString("uname"));
				user.setAddress(rs.getString("address"));
				user.setPhone(rs.getString("phone"));
				user.setRole(rs.getString("role"));

				rs.next();
				uList.add(user);
			}

			return uList;
	     }
		
	}
	 
	public User checkPsw(String username, String psw) {
		User u;
		try {
			u = jdbcTemplate.queryForObject("select username, uname, phone, address, role, email"
					+ " from user where username = ? AND password = ?", new Object[]{username,"{noop}"+psw}, new UserMapper()).get(0);
		}catch(Exception e) {
			u = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return u;
	}

	public User findById(String id) {
		User u = new User();
		try {
			u = jdbcTemplate.queryForObject("select username, uname, phone, address, role, email"
					+ " from user where username = ?", new Object[]{id}, new UserMapper()).get(0);
		}catch(Exception e) {
			u = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return u;
	}

	public int insert(User u){
		try{
			jdbcTemplate.update("insert into user (username, email, uname, address, phone, password, role)" +
					"values (?,?,?,?,?,?,?)", u.getUsername(), u.getEmail(), u.getUname(), u.getAddress(), u.getPhone(), "{noop}"+u.getPassword(), "ROLE_USER");
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

	public int checkRole(String id){
		String u =  jdbcTemplate.queryForMap("select role  from user where username = ?",id).get("role").toString();

		if(u.equals("ROLE_USER"))
			return 0;
		else
			return 1;
	}

	public int modifyUser(User u){

		try{
			jdbcTemplate.update("update user set email=?, uname=?, address=?, phone=?" +
					"where username=?", u.getEmail(), u.getUname(), u.getAddress(), u.getPhone(), u.getUsername());
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

}
