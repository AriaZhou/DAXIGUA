package com.example.demo.controller;

import com.example.demo.dao.UserDAO;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserDAO userDao;

    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView userIndex(Principal principal) {

        ModelAndView model = new ModelAndView("user/index");
        try{
            User u = userDao.findById(principal.getName());
            model.addObject("userInfo",u);
        }catch(Exception e){

        }

        return model;
    }

//    @RequestMapping("user/addOrder")
//    @ResponseBody
//    public ModelAndView addorder(){
//
//    }
}
