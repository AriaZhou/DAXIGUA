package com.example.demo;

import com.example.demo.API.KdGoldCreateOrderAPI;
import com.example.demo.API.KdniaoSubscribeAPI;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	OrderDAO orderDao;
	@Autowired
	UserDAO userDAO;

	@Test
	public void contextLoads() {

		KdGoldCreateOrderAPI api = new KdGoldCreateOrderAPI();
		User admin = userDAO.findById("897694163").get();
		try {
			Iterable<Orders> oLst = orderDao.findAll();
			for (Orders o:oLst) {
				if(o.getUser().getProvince()!=null){
					String result = api.orderOnlineByJson(o,admin);
					System.out.println(result);
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
