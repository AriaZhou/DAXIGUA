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

	@RequestMapping("/login")
	@ResponseBody
	public ModelAndView home(Principal principal) {

		ModelAndView model;
		try{
			System.out.println(principal.getName());
			if(userDao.checkRole(principal.getName())==0)
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
		System.out.println(cus.getUsername());
		int flag = userDao.insert(cus);
		ModelAndView model;
		if(flag == 0){
			model = new ModelAndView("index/register");
			model.addObject("registerError", true);
		}else{
			model = new ModelAndView("redirect:/login");
		}

		return model;
	}

	@RequestMapping("/login-error")
	public ModelAndView loginError(Model model) {
		model.addAttribute("loginError", true);
		return new ModelAndView("index/login");
	}
	
}
