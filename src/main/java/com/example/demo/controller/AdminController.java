package com.example.demo.controller;

import com.example.demo.UploadUtil;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    StateDAO stateDao;
    @Autowired
    PStateDAO pstateDao;
    @Autowired
    PaymentDAO paymentDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;  //注入bcryct加密

    @RequestMapping("/admin")
    @ResponseBody
    public ModelAndView adminIndex(Principal principal) {

        ModelAndView model;
        try{
            if(userDao.findById(principal.getName()).get().getRole().equals("0"))
                model = new ModelAndView("redirect:/index");
            else{
                model = new ModelAndView("admin/index");
                model.addObject("username", principal.getName());
            }
        }catch(Exception e){
            model = new ModelAndView("index/login");
        }

        return model;
    }

    @RequestMapping("/admin/revealAllGroup")
    @ResponseBody
    public ModelAndView revealAllGroup(Principal principal) {

        ModelAndView model = new ModelAndView("admin/groupLst");

        Iterable<Group> gLst = groupDao.findALLByState();
        model.addObject("gLst", gLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addGroup")
    @ResponseBody
    public String addGroup(@ModelAttribute Group g) {

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
    public String deleteGroup(String groupid){

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

    @RequestMapping("/admin/deleteGroupGroup")
    @ResponseBody
    public String deleteGroupGroup(String groupid){

        try{
            List<String> ids = new ArrayList<>();
            if(groupid.contains(","))
                ids = Arrays.asList(groupid.split(","));
            else
                ids.add(groupid);
            groupDao.deleteAll(groupDao.findAllById(ids));
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
    public String modifyGroup(@ModelAttribute Group g) {

        try{
//            p.setUsername(principal.getName());
            Group gg = groupDao.findById(g.getId()).get();
            //时间推后的话产品状态要跟着改
            if(gg.getEndtime().getTime() < new Date().getTime() && g.getEndtime().getTime() > new Date().getTime()){
                for(Product p : gg.getProducts()){
                    if(p.getPcount()>0){
                        p.setState(pstateDao.findById((long)0).get());
                        productDao.save(p);
                    }
                }
            }
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

        //超过时间的产品要修改状态
        Iterable<Group> gAfterLst = groupDao.findAllWithNowTimeAfter(new Date());
        Iterable<Product> pLst;
        for(Group g :gAfterLst){
            pLst = productDao.fingbygroupinsatte0(g.getId());
            for (Product p : pLst) {
                p.setState(pstateDao.findById((long) 1).get());
                productDao.save(p);
            }
        }

        pLst = productDao.findALLByState();
        model.addObject("pLst", pLst);
        Iterable<Group> gLst = groupDao.findAllWithNowTimeBefore(new Date());
        model.addObject("gLst", gLst);
        Iterable<PState> sLst = pstateDao.findAll();
        model.addObject("sLst", sLst);
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
//            p.setId(p.getGroup().getId()+now.getTime());
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
    public String deleteProduct(String productid){

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

    @RequestMapping("/admin/deleteProductGroup")
    @ResponseBody
    public String deleteProductGroup(String productid){

        try{
            List<String> ids = new ArrayList<>();
            if(productid.contains(","))
                ids = Arrays.asList(productid.split(","));
            else
                ids.add(productid);
            productDao.deleteAll(productDao.findAllById(ids));
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
            Product pOld = productDao.findById(p.getId()).get();
            p.setUploadTime(pOld.getUploadTime());
            p.setEnabled(pOld.getEnabled());
            productDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/exportSelectedProductData")
    public void exportSelectedProductData(HttpServletRequest request, HttpServletResponse response){
        String tempIds = request.getParameter("ids");

        if(tempIds.contains(",")){
            List<String> ids = Arrays.asList(tempIds.split(","));
            productService.exportProduct(ids,response);
        }else{
            List<String> ids = new ArrayList<>();
            ids.add(tempIds);
            productService.exportProduct(ids,response);
        }

    }

    @RequestMapping("/admin/exportProductData")
    public void exportProductData(HttpServletResponse response){
        productService.exportProduct(null,response);
    }

    @RequestMapping("/admin/revealAllOrder")
    @ResponseBody
    public ModelAndView revealAllOrder(Principal principal) {

        ModelAndView model = new ModelAndView("admin/orderLst");

        Iterable<Orders> oLst = orderDao.findALLByState();
        model.addObject("oLst", oLst);
        Iterable<Group> gLst = groupDao.findAllWithNowTimeBefore(new Date());
        model.addObject("gLst", gLst);
        Iterable<Product> pLst = productDao.findAll();
        model.addObject("pLst", pLst);
        Iterable<State> sLst = stateDao.findAll();
        model.addObject("sLst", sLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addOrder")
    @ResponseBody
    public String addOrder(@ModelAttribute Orders o) {

        try{
            orderService.addOrder(o);
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
    public String deleteOrder(String orderid){

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

    @RequestMapping("/admin/deleteOrderGroup")
    @ResponseBody
    public String deleteOrderGroup(String orderid){

        try{
            List<String> ids = new ArrayList<>();
            if(orderid.contains(","))
                ids = Arrays.asList(orderid.split(","));
            else
                ids.add(orderid);
            orderDao.deleteAll(orderDao.findAllById(ids));
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/modifyOrder")
    @ResponseBody
    public String modifyOrder(@ModelAttribute Orders o, Principal principal) {

        try{
            o.setUser(userDao.findById(principal.getName()).get());
            o.setTime(orderDao.findById(o.getId()).get().getTime());
            orderDao.save(o);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }

    @RequestMapping("/admin/modifyOrderState")
    @ResponseBody
    public String modifyOrderState(String id, Long state) {

        try{
            List<String> ids = new ArrayList<>();
            if(id.contains(","))
                ids = Arrays.asList(id.split(","));
            else
                ids.add(id);

            Iterable<Orders> oLSt = orderDao.findAllById(ids);
            for(Orders o:oLSt){
                o.setState(stateDao.findById(state).get());
            }
            orderDao.saveAll(oLSt);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }

    @RequestMapping("/admin/exportSelectedData")
    public void exportSelectedData(HttpServletRequest request, HttpServletResponse response){
        String tempIds = request.getParameter("ids");

        if(tempIds.contains(",")){
            List<String> ids = Arrays.asList(tempIds.split(","));
            orderService.export(ids,response);
        }else{
            List<String> ids = new ArrayList<>();
            ids.add(tempIds);
            orderService.export(ids,response);
        }

    }

    @RequestMapping("/admin/exportData")
    public void exportData(HttpServletResponse response){
        orderService.export(null,response);
    }

    @RequestMapping("/admin/revealAllUser")
    @ResponseBody
    public ModelAndView revealAllUser(Principal principal) {

        ModelAndView model = new ModelAndView("admin/userLst");

        Iterable<User> uLst = userDao.findAll();
        model.addObject("uLst", uLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addUser")
    @ResponseBody
    public String addUser(@ModelAttribute User u) {

        try{
            if(userDao.existsById(u.getUsername())){
                return "0";
            }
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            u.setRole("ROLE_USER");
            userDao.save(u);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    @RequestMapping("/admin/deleteUser")
    @ResponseBody
    public String deleteUser(String userid){

        try{
            userDao.deleteById(userid);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }

    }

    @RequestMapping("/admin/deleteUserGroup")
    @ResponseBody
    public String deleteUserGroup(String userid){

        try{
            List<String> ids = new ArrayList<>();
            if(userid.contains(","))
                ids = Arrays.asList(userid.split(","));
            else
                ids.add(userid);
            userDao.deleteAll(userDao.findAllById(ids));
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/modifyUser")
    @ResponseBody
    public String modifyUser(@ModelAttribute User u) {

        try{
            if(!u.getPassword().equals("00000000")){
                u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            }else {
                u.setPassword(userDao.findById(u.getUsername()).get().getPassword());
            }
            u.setRole("ROLE_USER");
            userDao.save(u);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }

    @RequestMapping("/admin/revealAllPayment")
    @ResponseBody
    public ModelAndView revealAllPayment(Principal principal) {

        ModelAndView model = new ModelAndView("admin/paymentLst");

        Iterable<Payment> pLst = paymentDao.findALLByState();
        for (Payment p:pLst) {
            p.setValue(p.getValue().replaceAll(" ","\n"));
        }
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/deletePayment")
    @ResponseBody
    public String deletePayment(String paymentid){

        try{
            paymentDao.deleteById(paymentid);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }

    }

    @RequestMapping("/admin/deletePaymentGroup")
    @ResponseBody
    public String deletePaymentGroup(String paymentid){

        try{
            List<String> ids = new ArrayList<>();
            if(paymentid.contains(","))
                ids = Arrays.asList(paymentid.split(","));
            else
                ids.add(paymentid);
            paymentDao.deleteAll(paymentDao.findAllById(ids));
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/setPaymentTrue")
    @ResponseBody
    public String setPaymentTrue(String paymentid, String actualPrice){

        try{
            Payment p = paymentDao.findById(paymentid).get();
            Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(p.getValue().split(" ")));

            for (Orders o : orders){
                o.setState(stateDao.findById((long)1).get());
//                for(Payment pp : paymentDao.findbyOrderid("%"+o.getId()+"%"))
//                    paymentDao.delete(pp);
            }
            orderDao.saveAll(orders);
            p.setState(2);
            p.setActualprice(Double.parseDouble(actualPrice));
            paymentDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/setPaymentFalse")
    @ResponseBody
    public String setPaymentFalse(String paymentid){

        try{
            Payment p = paymentDao.findById(paymentid).get();
            Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(p.getValue().split(" ")));

            for (Orders o : orders)
                o.setState(stateDao.findById((long)0).get());

            orderDao.saveAll(orders);
            p.setState(0);
            p.setActualprice(0);
            paymentDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    /**
     * 数据上传导入
     * @param file
     */
    @RequestMapping(value = "/admin/uploadpayment", method = RequestMethod.POST)
    @ResponseBody
    public void uploadExcel(@RequestParam("excelFile") MultipartFile file, Principal principal, HttpServletRequest request, HttpServletResponse response){
        int num=0;
        try {
            List<Payment>list= paymentService.paymentImport(file.getOriginalFilename(),file,response);
            for (Payment p : list)
                num++;

            //num=sListService.doUploadFile(list);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入失败");
        }
    }

    @RequestMapping("/admin/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){

        paymentService.exportTemplate(request,response);

    }

}
