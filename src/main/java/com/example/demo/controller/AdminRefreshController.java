package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Date;

@Controller
public class AdminRefreshController {

    @Autowired
    OrderDAO orderDao;
    @Autowired
    GroupDAO groupDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    PStateDAO pstateDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    PaymentDAO paymentDao;

    @RequestMapping("/admin/loadMoreOrder")
    public String loadMoreOrder(Model model, String param, int page) {

        Iterable<Orders> oLst;
        Pageable pageable = PageRequest.of(page, 20);
        if(param==null)
            oLst = orderDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            oLst = orderDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }
        model.addAttribute("oLst", oLst);

        return "admin/orderLst :: orderDetailList";
    }

    @RequestMapping("/admin/loadMoreGroup")
    public String loadMoreGroup(Model model, String param, int page) {

        Iterable<Group> gLst;
        Pageable pageable = PageRequest.of(page, 20);
        if(param==null)
            gLst = groupDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            gLst = groupDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }

        model.addAttribute("gLst", gLst);

        return "admin/groupLst :: groupDetailList";
    }

    @RequestMapping("/admin/loadMoreProduct")
    public String loadMoreProduct(Model model, String param, int page) {

        //超过时间的产品要修改状态
        Iterable<Group> gAfterLst = groupDao.findAllWithNowTimeAfter(new Date());
        Iterable<Product> pLst;
        for(Group g :gAfterLst){
            pLst = productDao.fingbygroupinsatte0(g.getId());
            for (Product p : pLst) {
                p.setState(pstateDao.findById(Product.PSTATE_OVERSALE).get());
                productDao.save(p);
            }
        }

        Pageable pageable = PageRequest.of(page, 20);
        if(param==null)
            pLst = productDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            pLst = productDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }

        model.addAttribute("pLst", pLst);

        return "admin/productLst :: productDetailList";
    }

    @RequestMapping("/admin/loadMoreUser")
    public String loadMoreUser(Model model, String param, int page, Principal principal) {

        Iterable<User> uLst;
        Pageable pageable = PageRequest.of(page, 20);
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

        model.addAttribute("uLst", uLst);
        model.addAttribute("username", principal.getName());

        return "admin/userLst :: userDetailList";
    }

    @RequestMapping("/admin/loadMorePayment")
    public String loadMorePayment(Model model, String param, int page, Principal principal) {

        Iterable<Payment> pLst;
        Pageable pageable = PageRequest.of(page, 20);
        if(param==null)
            pLst = paymentDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            pLst = paymentDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }

        model.addAttribute("pLst", pLst);
        if(param==null)
            model.addAttribute("searchbar", "");
        else
            model.addAttribute("searchbar", param);
        model.addAttribute("username", principal.getName());

        return "admin/paymentLst :: paymentDetailList";
    }

}
