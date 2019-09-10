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
	public ModelAndView index(Principal principal) {

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
			cus.setPassword("{noop}"+cus.getPassword());
			userDao.save(cus);
			model = new ModelAndView("redirect:/login");
		}catch (Exception e){
			model = new ModelAndView("index/register");
			model.addObject("registerError", true);
		}

		return model;
	}

	@RequestMapping("/login-error")
	public ModelAndView loginError(Model model) {
		model.addAttribute("loginError", true);
		return new ModelAndView("index/login");
	}
	
}
