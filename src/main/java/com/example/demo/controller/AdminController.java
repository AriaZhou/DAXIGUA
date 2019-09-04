package com.example.demo.controller;

import com.example.demo.dao.GroupDAO;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Group;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    OrderDAO orderDao;
    @Autowired
    GroupDAO groupDao;

    @RequestMapping("/admin")
    @ResponseBody
    public ModelAndView adminIndex(Principal principal) {

        try{
            System.out.println(principal.getName());
        }catch(Exception e){

        }
        ModelAndView model = new ModelAndView("admin/index");

        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/revealAllGroup")
    @ResponseBody
    public ModelAndView revealAllGroup(Principal principal) {

        ModelAndView model = new ModelAndView("admin/groupLst");

        Iterable<Group> gLst = groupDao.findAll();
        model.addObject("gLst", gLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addGroup")
    @ResponseBody
    public String addGroup(@ModelAttribute Group g, Principal principal) {

        try{
//            p.setUsername(principal.getName());
//            Date now = new Date();
//            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//            p.setUploadTime(format.format(now));
//            p.setId(p.getGroup().getId()+now.getTime());
            groupDao.save(g);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    @RequestMapping("/admin/deleteGroup")
    @ResponseBody
    public String deleteGroup(String groupid, Principal principal){

        try{
            groupDao.deleteById(groupid);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/modifyGroup")
    @ResponseBody
    public String modifyGroup(@ModelAttribute Group g, Principal principal) {

        try{
//            p.setUsername(principal.getName());
            groupDao.save(g);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/revealAllProduct")
    @ResponseBody
    public ModelAndView revealAllProduct(Principal principal) {

        ModelAndView model = new ModelAndView("admin/productLst");

        Iterable<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);
        Iterable<Group> gLst = groupDao.findAll();
        model.addObject("gLst", gLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addProduct")
    @ResponseBody
    public String addProduct(@ModelAttribute Product p, Principal principal) {

        try{
            p.setUsername(principal.getName());
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            p.setUploadTime(format.format(now));
            p.setId(p.getGroup().getId()+now.getTime());
            productDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    @RequestMapping("/admin/deleteProduct")
    @ResponseBody
    public String deleteProduct(String productid, Principal principal){

        try{
            productDao.deleteById(productid);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/modifyProduct")
    @ResponseBody
    public String modifyProduct(@ModelAttribute Product p, Principal principal) {

        try{
            p.setUsername(principal.getName());
            productDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/revealAllOrder")
    @ResponseBody
    public ModelAndView revealAllOrder(Principal principal) {

        ModelAndView model = new ModelAndView("admin/orderLst");

        Iterable<Orders> oLst = orderDao.findAll();
        model.addObject("oLst", oLst);
        Iterable<Group> gLst = groupDao.findAll();
        model.addObject("gLst", gLst);
        Iterable<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addOrder")
    @ResponseBody
    public String addOrder(@ModelAttribute Orders o, String productId, Principal principal) {

        try{
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            o.setTime(format.format(now));
            o.setId(o.getUser().getUsername()+now.getTime());
            orderDao.save(o);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    @RequestMapping("/admin/deleteOrder")
    @ResponseBody
    public String deleteOrder(String orderid, Principal principal){

        try{
            orderDao.deleteById(orderid);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }

    }

    @RequestMapping("/admin/modifyOrder")
    @ResponseBody
    public String modifyOrder(@ModelAttribute Orders o, Principal principal) {

        try{
            o.setUser(userDao.findById(principal.getName()).get());
            orderDao.save(o);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }


}
