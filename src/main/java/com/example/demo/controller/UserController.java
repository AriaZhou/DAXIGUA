package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
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
    @Autowired
    PaymentDAO paymentDao;
    @Autowired
    private OrderService orderService;


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
            orderService.addOrder(pId, count, principal);
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

            for (int i = 0; i < pids.size(); i++)
                orderService.addOrder(pids.get(i), Integer.parseInt(counts.get(i)), principal);

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

    @RequestMapping(value = "/user/addPayment", method = RequestMethod.POST)
    @ResponseBody
    public String addPayment(String ids, double price, Principal principal){
        long id = Long.parseLong(new Date().getTime()+""+Integer.parseInt(principal.getName()));
        StringBuilder remark = new StringBuilder();
        while(id>0) {
            long tmp = id % 62;
            if (tmp < 10)
                remark.append(tmp);
            else if (tmp < 36)
                remark.append((char) (tmp + 87));
            else
                remark.append((char) (tmp + 29));
            id = id / 62;
        }
        Payment p = new Payment();
        p.setId(remark.toString());
        p.setTotprice(price);
        p.setState(0);
        p.setValue(ids);
        p.setUser(userDao.findById(principal.getName()).get());

        paymentDao.save(p);
        return remark.toString();

    }
}
