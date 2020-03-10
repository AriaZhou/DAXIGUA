package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.example.demo.Variable;

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
    PTypeDAO pTypeDAO;
    @Autowired
    PaStateDAO pastateDao;
    @Autowired
    PaymentDAO paymentDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;

    //显示所有正在开的团
    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView userIndex(Principal principal) {

        ModelAndView model = new ModelAndView("user/myOrder");
//        ModelAndView model = new ModelAndView("user/index");
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

    //显示所有货物
    @RequestMapping("/user/revealProduct")
    @ResponseBody
    public ModelAndView revealProduct(Principal principal, String groupid) {

        ModelAndView model = new ModelAndView("user/productLst");
        try{
            User u = userDao.findById(principal.getName()).get();
            model.addObject("userInfo",u);

            Iterable<Product> pLst = productDao.findByGroupIdVali(groupid);
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

    //批量添加货物
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

    //我的订单
    @RequestMapping("/user/myOrder")
    @ResponseBody
    public ModelAndView myOrder(Principal principal){

        ModelAndView model = new ModelAndView("user/myOrder");
        User u = userDao.findById(principal.getName()).get();
        model.addObject("userInfo",u);

        Iterable<Orders> orderLst = orderDao.findbyidAndOrderByState(principal.getName());
        model.addObject("orderLst",orderLst);

        return model;
    }

    @RequestMapping("/user/deleteOrder")
    @ResponseBody
    public String deleteOrder(String orderid, Principal principal){

        Orders o = orderDao.findById(orderid).get();
        Product p = productDao.findProductForUpdate(o.getProduct().getId());
        p.setPcount(p.getPcount()+o.getOcount());
        p.setOrdercount(p.getOrdercount()-o.getOcount());
        productDao.save(p);
        orderDao.deleteById(orderid);
        return "1";

    }

    @RequestMapping(value = "/user/modifyUser", method = RequestMethod.POST)
    @ResponseBody
    public String modifyUser(@ModelAttribute User u){
        User uOld = userDao.findById(u.getUsername()).get();
        if(u.getProvince().isEmpty()){
            u.setProvince(uOld.getProvince());
        }
        if(u.getCity().isEmpty()){
            u.setCity(uOld.getCity());
        }
        if(u.getDistrict().isEmpty()){
            u.setDistrict(uOld.getDistrict());
        }
        u.setRole(uOld.getRole());
        u.setPassword(uOld.getPassword());
        userDao.save(u);
        return "1";

    }

    //添加货物支付订单
    @RequestMapping(value = "/user/addPayment", method = RequestMethod.POST)
    @ResponseBody
    public String addPayment(String ids, double price, Principal principal){

        return paymentService.addPayment(ids,price,principal);

    }

    //添加运费支付订单
    @RequestMapping(value = "/user/addFreightPayment", method = RequestMethod.POST)
    @ResponseBody
    public String addFreightPayment(String ids, int type, Principal principal){

        return paymentService.addFreightPayment(type,ids,principal);

    }

    //我的支付订单
    @RequestMapping("/user/myPayment")
    @ResponseBody
    public ModelAndView myPayment(Principal principal){

        ModelAndView model = new ModelAndView("user/myPayment");
        User u = userDao.findById(principal.getName()).get();
        model.addObject("userInfo",u);

        //插入换行符，让界面更好看
        Iterable<Payment> payLst = paymentDao.findbyidAndOrderByState(principal.getName());
        for (Payment p:payLst) {
            p.setValue(p.getValue().replaceAll(" ","\n"));
        }
        model.addObject("payLst",payLst);

        return model;
    }

    //删除支付订单
    @RequestMapping("/user/deletePayment")
    @ResponseBody
    public String deletePayment(String payid, Principal principal){

        Payment p = paymentDao.findById(payid).get();
        Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(paymentDao.findById(payid).get().getValue().split(" ")));
        for (Orders o : orders){
            //判断支付类别，回滚订单状态
            if(p.getType().getId()==Payment.PASTYPE_PRODUCT){
                o.setState(stateDao.findById(Orders.OSTATE_UNPAID).get());
            }else{
                o.setState(stateDao.findById(Orders.OSTATE_PRODUCT_PAID).get());
            }
        }

        orderDao.saveAll(orders);
        paymentDao.deleteById(payid);
        return "1";

    }

}
