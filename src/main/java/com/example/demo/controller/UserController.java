package com.example.demo.controller;

import com.example.demo.dao.*;
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
import java.util.Arrays;
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
    @Autowired
    StateDAO stateDao;


    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView userIndex(Principal principal) {

        ModelAndView model = new ModelAndView("user/index");
        try{
            User u = userDao.findById(principal.getName()).get();
            model.addObject("userInfo",u);

            Iterable<Group> groupLst = groupDao.findAllWithNowTimeBefore(new Date());
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
            o.setId(now.getTime()+"");
            o.setState(stateDao.findById(0L).get());
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

    @RequestMapping(value = "/user/addOrderGroup", method = RequestMethod.POST)
    @ResponseBody
    public String addOrderGroup(String productid, String productNum, Principal principal){

        try{
            Orders o = new Orders();
            o.setUser(userDao.findById(principal.getName()).get());

            List<String> pids = new ArrayList<>();
            List<String> counts = new ArrayList<>();
            if(productid.contains(",")){
                pids = Arrays.asList(productid.split(","));
                counts = Arrays.asList(productNum.split(","));
            }
            else{
                pids.add(productid);
                counts.add(productNum);
            }

            for (int i = 0; i < pids.size(); i++) {
                Product p = productDao.findById(pids.get(i)).get();
                o.setOcount(Integer.parseInt(counts.get(i)));
                o.setProduct(p);
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                o.setTime(format.format(now));
                o.setId(now.getTime()+"");
                o.setState(stateDao.findById(0L).get());
                o.setPrice(Integer.parseInt(productDao.findById(pids.get(i)).get().getPrice())*Integer.parseInt(counts.get(i))+"");
                orderDao.save(o);
                p.setPcount(p.getPcount()-Integer.parseInt(counts.get(i)));
                productDao.save(p);
            }
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
        User uOld = userDao.findById(u.getUsername()).get();
        u.setRole(uOld.getRole());
        u.setPassword(uOld.getPassword());
        userDao.save(u);
        return "1";

    }
}
