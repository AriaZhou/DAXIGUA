package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class AdminUserController {

    @Autowired
    UserDAO userDao;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;  //注入bcryct加密

    @RequestMapping("/admin/revealAllUser")
    @ResponseBody
    public ModelAndView revealAllUser(Principal principal) {

        ModelAndView model = new ModelAndView("admin/userLst");

        Pageable pageable = PageRequest.of(0, 20);
        Iterable<User> uLst = userDao.findALLByState(pageable).getContent();
//        Iterable<User> uLst = userDao.findAll();
//        for(User u :uLst){
//            if(u.getUsername() != principal.getName())
//                u.setPassword(bCryptPasswordEncoder.encode(u.getUsername()));
//        }
        userDao.saveAll(uLst);
        model.addObject("uLst", uLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/revealUserByParam")
    @ResponseBody
    public ModelAndView revealUserByParam(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/userLst");

        Iterable<User> uLst;
        Pageable pageable = PageRequest.of(0, 20);
        if(param==null)
            uLst = userDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            uLst = userDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }

        model.addObject("uLst", uLst);
        if(param==null)
            model.addObject("searchbar", "");
        else
            model.addObject("searchbar", param);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addUser")
    @ResponseBody
    public String addUser(@ModelAttribute User u) {

        try{
            if(userDao.existsById(u.getUsername())){
                return "0";
            }
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            u.setRole("ROLE_USER");
            userDao.save(u);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    //批量删除使用者
    @RequestMapping("/admin/deleteUser")
    @ResponseBody
    public String deleteUser(String userid){

        try{
            List<String> ids = new ArrayList<>();
            if(userid.contains(","))
                ids = Arrays.asList(userid.split(","));
            else
                ids.add(userid);
            userDao.deleteAll(userDao.findAllById(ids));
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/modifyUser")
    @ResponseBody
    public String modifyUser(@ModelAttribute User u) {

        try{
            User uOld = userDao.findById(u.getUsername()).get();

            if(!u.getPassword().equals("00000000")){
                u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            }else {
                u.setPassword(uOld.getPassword());
            }
            if(u.getProvince().isEmpty()){
                u.setProvince(uOld.getProvince());
            }
            if(u.getCity().isEmpty()){
                u.setCity(uOld.getCity());
            }
            if(u.getDistrict().isEmpty()){
                u.setDistrict(uOld.getDistrict());
            }
            u.setRole("ROLE_USER");
            userDao.save(u);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }

}
