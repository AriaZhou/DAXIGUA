package com.example.demo.dao;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProductDAODB implements ProductDAO{

	//@Autowired
	//private DataSource dataSource;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final class ProductMapper implements RowMapper<List<Product>>{
		
		public List<Product> mapRow(ResultSet rs, int rowNum) throws SQLException {

			List<Product> productLst = new ArrayList<>();
			for(int i= 0; i < rs.getRow(); i++){
				Product product = new Product();
				product.setId(rs.getString("id"));
				product.setUsername(rs.getString("username"));
				product.setUoloadTime(rs.getString("uploadtime"));
				product.setName(rs.getString("pname"));
				product.setPrice(rs.getString("price"));
				product.setCount(rs.getInt("pcount"));
				product.setDescription(rs.getString("description"));
				product.setStartTime(rs.getString("starttime"));
				product.setEndTime(rs.getString("endtime"));
				product.setEnabled(rs.getInt("enabled"));

				rs.next();
				productLst.add(product);
			}

			return productLst;
	     }
		
	}

	public List<Product> findAll() {
		List<Product> p;
		try {
			p = jdbcTemplate.queryForObject("select id, username, uploadtime, pname, price, pcount, description,"
					+ "starttime, endtime, enabled from product ", new ProductMapper());
		}catch(Exception e) {
			p = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return p;
	}


	public Product findById(String id) {
		Product p;
		try {
			p = jdbcTemplate.queryForObject("select id, username, uploadtime, pname, price, pcount, description,"
					+ "starttime, endtime, enabled from product where id = ?", new Object[]{id}, new ProductMapper()).get(0);
		}catch(Exception e) {
			p = null;
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
		}
		return p;
	}

	public int insert(Product p){
		try{
			jdbcTemplate.update("insert into product (id, username, uploadtime, pname, price, pcount, description, starttime, endtime, enabled)" +
					"values (?,?,?,?,?,?,?,?,?,?)", p.getId(), p.getUsername(), new Date(), p.getName(), p.getPrice(), p.getCount(), p.getDescription(),
					p.getStartTime(), p.getEndTime(), 1);
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
			jdbcTemplate.update("delete from product where id = ?", id);
			return 1;
		}catch(Exception e){
			System.out.println("----error----");
			System.out.println(e.getMessage());
			System.out.println("----error----");
			return 0;
		}
	}

}
