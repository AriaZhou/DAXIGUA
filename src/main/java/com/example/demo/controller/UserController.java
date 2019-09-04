package com.example.demo.controller;

import com.example.demo.dao.GroupDAO;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    OrderDAO orderDao;
    @Autowired
    GroupDAO groupDao;


    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView userIndex(Principal principal) {

        ModelAndView model = new ModelAndView("user/index");
        try{
            User u = userDao.findById(principal.getName()).get();
            model.addObject("userInfo",u);

            Iterable<Group> groupLst = groupDao.findAll();
            model.addObject("groupLst",groupLst);

        }catch(Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
        }

        return model;
    }

    @RequestMapping("/user/revealProduct")
    @ResponseBody
    public ModelAndView revealProduct(Principal principal, String groupid) {

        ModelAndView model = new ModelAndView("user/productLst");
        try{
            User u = userDao.findById(principal.getName()).get();
            model.addObject("userInfo",u);

            Iterable<Product> pLst = productDao.findByGroupId(groupid);
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

        try{
            Orders o = new Orders();
            Product p = productDao.findById(pId).get();
            o.setUser(userDao.findById(principal.getName()).get());
            o.setOcount(count);
            o.setProduct(p);
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            o.setTime(format.format(now));
            o.setId(principal.getName()+now.getTime());
            o.setState(0);
            o.setPrice(Integer.parseInt(productDao.findById(pId).get().getPrice())*count+"");
            orderDao.save(o);
            p.setPcount(p.getPcount()-count);
            productDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
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

        List<String> s = new ArrayList<String>();
        s.add("下单");
        s.add("已付款");
        s.add("出货中");
        s.add("申请退款");
        s.add("已退款");
        Iterable<String> stateLst = s;
        model.addObject("stateLst",stateLst);

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
