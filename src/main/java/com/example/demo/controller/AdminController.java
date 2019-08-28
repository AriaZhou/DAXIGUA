package com.example.demo.controller;

import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    OrderDAO orderDao;

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

    @RequestMapping("/admin/revealAllProduct")
    @ResponseBody
    public ModelAndView revealAllProduct(Principal principal) {

        ModelAndView model = new ModelAndView("admin/productLst");

        List<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addProduct")
    @ResponseBody
    public String addProduct(Product p, Principal principal) {

        p.setUsername(principal.getName());
        return productDao.insert(p)+"";
    }

    @RequestMapping("/admin/deleteProduct")
    @ResponseBody
    public String deleteProduct(String productid, Principal principal){

        return productDao.deleteById(productid)+"";

    }

    @RequestMapping("/admin/modifyProduct")
    @ResponseBody
    public String modifyProduct(Product p, Principal principal) {

        p.setUsername(principal.getName());
        return productDao.modifyProduct(p)+"";
    }

    @RequestMapping("/admin/revealAllOrder")
    @ResponseBody
    public ModelAndView revealAllOrder(Principal principal) {

        ModelAndView model = new ModelAndView("admin/orderLst");

        List<Order> oLst = orderDao.findAll();
        model.addObject("oLst", oLst);
        List<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addOrder")
    @ResponseBody
    public String addOrder(Order o, Principal principal) {

        o.setUsername(principal.getName());
        return orderDao.insert(o)+"";
    }

    @RequestMapping("/admin/deleteOrder")
    @ResponseBody
    public String deleteOrder(String orderid, Principal principal){

        return orderDao.deleteById(orderid)+"";

    }

    @RequestMapping("/admin/modifyOrder")
    @ResponseBody
    public String modifyOrder(Order o, Principal principal) {

        o.setUsername(principal.getName());
        return orderDao.modifyOrder(o)+"";
    }


}
