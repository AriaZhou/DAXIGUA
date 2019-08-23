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
public class AdminController {

    @Autowired
    UserDAO userDao;

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

}
