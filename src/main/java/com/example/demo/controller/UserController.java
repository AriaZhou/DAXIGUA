package com.example.demo.controller;

import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    OrderDAO orderDao;


    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView userIndex(Principal principal) {

        ModelAndView model = new ModelAndView("user/index");
        try{
            User u = userDao.findById(principal.getName()).get();
            model.addObject("userInfo",u);

            List<Product> pLst = productDao.findAll();
            model.addObject("productLst",pLst);

        }catch(Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
        }

        return model;
    }

    @RequestMapping(value = "/user/addOrder", method = RequestMethod.POST)
    @ResponseBody
    public String addOrder(String pId, int count, Principal principal){

        Orders o = new Orders();
        o.setUser(userDao.findById(principal.getName()).get());
        o.setCount(count);
        o.setProduct(productDao.findById(pId));
        o.setPrice(Integer.parseInt(productDao.findById(pId).getPrice())*count+"");

        return orderDao.save(o)+"";
    }

    @RequestMapping("/user/modifyData")
    @ResponseBody
    public ModelAndView modifyData(Principal principal){

        ModelAndView model = new ModelAndView("user/myPage");
        User u = userDao.findById(principal.getName()).get();
        model.addObject("userInfo",u);

        return model;
    }

    @RequestMapping("/user/myOrder")
    @ResponseBody
    public ModelAndView myOrder(Principal principal){

        ModelAndView model = new ModelAndView("user/myOrder");
        User u = userDao.findById(principal.getName()).get();
        model.addObject("userInfo",u);

        Iterable<Orders> orderLst = userDao.findById(principal.getName()).get().getOrders();
        model.addObject("orderLst",orderLst);

        return model;
    }

    @RequestMapping("/user/deleteOrder")
    @ResponseBody
    public String deleteOrder(String orderid, Principal principal){

        orderDao.deleteById(orderid);
        return "1";

    }

    @RequestMapping(value = "/user/modifyUser", method = RequestMethod.POST)
    @ResponseBody
    public String modifyUser(@ModelAttribute User u){

        userDao.save(u);
        return "1";

    }
}
