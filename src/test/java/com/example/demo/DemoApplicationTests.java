package com.example.demo;

import com.example.demo.API.KdGoldCreateOrderAPI;
import com.example.demo.API.KdniaoSubscribeAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
