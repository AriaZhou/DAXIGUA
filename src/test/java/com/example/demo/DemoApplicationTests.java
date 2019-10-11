package com.example.demo;

<<<<<<< HEAD
import com.example.demo.service.OrderService;
=======
import com.example.demo.API.KdGoldCreateOrderAPI;
import com.example.demo.API.KdniaoSubscribeAPI;
>>>>>>> e81e597f6e06bbb637d9b59558c2762b8b47fbea
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.Principal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

		KdGoldCreateOrderAPI api = new KdGoldCreateOrderAPI();
		try {
			String result = api.orderOnlineByJson();
			System.out.print(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
