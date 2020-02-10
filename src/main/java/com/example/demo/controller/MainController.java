package com.example.demo.controller;

import java.security.Principal;
import java.util.Arrays;

import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;;import javax.annotation.Resource;

@RestController
public class MainController {

	UserDAO userDao;
	@Autowired
	OrderDAO orderDao;
	@Autowired
	PaymentDAO paymentDao;

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;  //注入bcryct加密
	
	@RequestMapping(value={"/"})
	@ResponseBody
	public ModelAndView index(Principal principal) {

		ModelAndView model;
		try{
			if(userDao.findById(principal.getName()).get().getRole().equals("ROLE_USER"))
				model = new ModelAndView("redirect:/index");
			else
				model = new ModelAndView("redirect:/admin");
		}catch(Exception e){
			model = new ModelAndView("index/login");
		}

		return model;
	}

	@RequestMapping("/login")
	@ResponseBody
	public ModelAndView home(Principal principal) {

		ModelAndView model;
		try{
			if(userDao.findById(principal.getName()).get().getRole().equals("0"))
				model = new ModelAndView("redirect:/index");
			else
				model = new ModelAndView("redirect:/admin");
		}catch(Exception e){
			model = new ModelAndView("index/login");
		}

		return model;
	}

	@RequestMapping("/register")
	@ResponseBody
	public ModelAndView register(Principal principal) {

		ModelAndView model;
		try{
			System.out.println(principal.getName());
			model = new ModelAndView("redirect:/index");
		}catch(Exception e){
			model = new ModelAndView("index/register");
		}

		return model;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView register(@ModelAttribute User cus) {
		ModelAndView model;
		try{
			if(userDao.existsById(cus.getUsername())){
				model = new ModelAndView("index/register");
				model.addObject("registerError", true);
				return model;
			}

			cus.setRole("ROLE_USER");
			cus.setPassword(bCryptPasswordEncoder.encode(cus.getPassword()));
			userDao.save(cus);
			model = new ModelAndView("redirect:/login");
		}catch (Exception e){
			model = new ModelAndView("index/register");
			model.addObject("registerError", true);
			System.out.println(e.getMessage());
		}

		return model;
	}

	@RequestMapping("/login-error")
	public ModelAndView loginError(Model model) {
		model.addAttribute("loginError", true);
		return new ModelAndView("index/login");
	}

	@RequestMapping("/user/modifyPassword")
	public String modifyPassword(String oldPsw, String newPsw, Principal principal) {
		User u = userDao.findById(principal.getName()).get();
		if(!bCryptPasswordEncoder.matches(oldPsw,u.getPassword()))
			return "密码错误，修改失败";

		u.setPassword(bCryptPasswordEncoder.encode(newPsw));
		userDao.save(u);
		return "修改成功";
	}

	@RequestMapping("/searchPaymentDetail")
	@ResponseBody
	public String searchPaymentDetail(String payid){

		String detail = "";
		Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(paymentDao.findById(payid).get().getValue().split(" ")));
		for (Orders o : orders)
			detail+=o.getProduct().getGroup().getId() + "：  " + o.getProduct().getPname()+" ✖️"+o.getOcount()+"\n";

		orderDao.saveAll(orders);
		return detail;

	}
	
}
