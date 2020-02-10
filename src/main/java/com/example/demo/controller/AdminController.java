package com.example.demo.controller;

import com.example.demo.UploadUtil;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AdminController {

    @Autowired
    UserDAO userDao;

    @RequestMapping("/admin")
    @ResponseBody
    public ModelAndView adminIndex(Principal principal) {

        ModelAndView model;
        try{
            if(userDao.findById(principal.getName()).get().getRole().equals("0"))
                model = new ModelAndView("redirect:/index");
            else{
                model = new ModelAndView("admin/index");
                model.addObject("username", principal.getName());
            }
        }catch(Exception e){
            model = new ModelAndView("index/login");
        }

        return model;
    }
}
