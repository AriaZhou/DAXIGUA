package com.example.demo.controller;

import java.security.Principal;

import com.example.demo.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;;

@RestController
public class MainController {

	@Autowired
//	UserDAODB userDao;
	UserDAO userDao;
	
	@RequestMapping("/")
	@ResponseBody
	public ModelAndView index() {

		ModelAndView model = new ModelAndView("redirect:/index");
		return model;
	}

	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView userIndex(Principal principal) {

		ModelAndView model = new ModelAndView("user/index");
		try{
			User u = userDao.findById(principal.getName());
//			if(u.getRole().equals("ADMIN"))
//				model = new ModelAndView("redirect:/admin");
			model.addObject("userInfo",u);
		}catch(Exception e){

		}

		return model;
	}

	@RequestMapping("/admin")
	@ResponseBody
	public ModelAndView adminIndex(Principal principal) {

		try{
			System.out.println(principal.getName());
		}catch(Exception e){

		}
		ModelAndView model = new ModelAndView("admin/index");

		return model;
	}

	@RequestMapping("/login")
	@ResponseBody
	public ModelAndView home() {

		ModelAndView model = new ModelAndView("index/login");

		return model;
	}

	@RequestMapping("/register")
	@ResponseBody
	public ModelAndView register() {

		ModelAndView model = new ModelAndView("index/register");

		return model;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView register(@ModelAttribute User cus) {
		System.out.println(cus.getUsername());
		int flag = userDao.insert(cus);
		ModelAndView model;
		if(flag == 0){
			model = new ModelAndView("redirect:/register");
			model.addObject("registerError", true);
		}else
			model = new ModelAndView("redirect:/login");

		return model;
	}
//
//	@RequestMapping(value = "/user/index",method = RequestMethod.GET)
//	public ModelAndView uSignIn(@ModelAttribute User user) throws SQLException{
//		System.out.println("???????");
//		User u = userDao.checkPsw(user.getUsername(),user.getPassword());
//		if(u==null) {
//			System.out.print("not found");
//			return new ModelAndView("user/index");
//		}else {
//			System.out.print("find it!");
////			return principal.getName();
////			System.out.println(principal.getName());
//			return new ModelAndView("user/index");
//		}
//
//	}
	
//	@RequestMapping(value = "/userSignUp",method = RequestMethod.POST)
//	public String uSignUp(@ModelAttribute User user) throws SQLException{
//
//		User u = userDao.checkPsw(user.getPhone(),user.getPassword());
//		if(u==null) {
//			System.out.print("not found");
//			return "0";
//		}else {
//			System.out.print("find it!");
//			return "1";
//		}
//
//	}

	@RequestMapping("/login-error")
	public ModelAndView loginError(Model model) {
		model.addAttribute("loginError", true);
		return new ModelAndView("index/login");
	}
	
}
