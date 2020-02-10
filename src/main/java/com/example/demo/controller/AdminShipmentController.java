package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminShipmentController {

    @Autowired
    OrderDAO orderDao;
    @Autowired
    StateDAO stateDao;
    @Autowired
    PaStateDAO pastateDao;
    @Autowired
    PaymentDAO paymentDao;

    @RequestMapping("/admin/revealAllShipment")
    @ResponseBody
    public ModelAndView revealAllShipment(Principal principal) {

        ModelAndView model = new ModelAndView("admin/shipmentLst");

        Iterable<Payment> pLst = paymentDao.findALLReadyShip();
        for (Payment p:pLst) {
            p.setValue(p.getValue().replaceAll(" ","\n"));
        }
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());

        return model;
    }

    //出货
    @RequestMapping("/admin/setShipmentTrue")
    @ResponseBody
    public String setShipmentTrue(String paymentid, String actualPrice){

        try{
            Payment p = paymentDao.findById(paymentid).get();
            Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(p.getValue().split(" ")));

            for (Orders o : orders){
                o.setState(stateDao.findById(Orders.OSTATE_SHIPMENT).get());
            }
            orderDao.saveAll(orders);
            p.setState(pastateDao.findById(Payment.PASTATE_DONE).get());
            paymentDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

}
