package com.example.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public class UserDAODB implements UserDAO{

	//@Autowired
	//private DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final class UserMapper implements RowMapper<User>{
		
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			 User user = new User();
			 user.setUsername(rs.getString("username"));
			 user.setName(rs.getString("name"));
			 user.setAddress(rs.getString("address"));
			 user.setPhone(rs.getString("phone"));
			 user.setRole(rs.getString("role"));
	         return user;
	     }
		
	}
	 
	public User checkPsw(String username, String psw) {
		User u = new User();
		try {
			u = jdbcTemplate.queryForObject("select username, name, phone, address, role"
					+ " from user where username = ? AND password = ?", new Object[]{username,"{noop}"+psw}, new UserMapper());
		}catch(Exception e) {
			u = null;
		}
		return u;
	}

	public User findById(String id) {
		User u = new User();
		try {
			u = jdbcTemplate.queryForObject("select username, name, phone, address, role"
					+ " from user where username = ?", new Object[]{id}, new UserMapper());
		}catch(Exception e) {
			u = null;
		}
		return u;
	}

	public int insert(User u){
		try{
			jdbcTemplate.update("insert into user (username, email, name, address, phone, password, role)" +
					"values (?,?,?,?,?,?,?)", u.getUsername(), u.getEmail(), u.getName(), u.getAddress(), u.getPhone(), u.getPassword(), "ROLE_USER");
			return 1;
		}catch(Exception e){
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

}
