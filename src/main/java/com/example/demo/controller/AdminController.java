package com.example.demo.controller;

import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
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

    @RequestMapping("/admin")
    @ResponseBody
    public ModelAndView adminIndex(Principal principal) {

        try{
            System.out.println(principal.getName());
        }catch(Exception e){

        }
        ModelAndView model = new ModelAndView("admin/index");

        List<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);

        return model;
    }

    @RequestMapping("/admin/revealAllProduct")
    @ResponseBody
    public ModelAndView revealAllProduct(Principal principal) {

        ModelAndView model = new ModelAndView("admin/productLst");

        List<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);

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

        System.out.println(productid);
        return productDao.deleteById(productid)+"";

    }


}
