package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AdminPaymentController {

    @Autowired
    OrderDAO orderDao;
    @Autowired
    StateDAO stateDao;
    @Autowired
    PaStateDAO pastateDao;
    @Autowired
    PaymentDAO paymentDao;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping("/admin/revealAllPayment")
    @ResponseBody
    public ModelAndView revealAllPayment(Principal principal) {

        ModelAndView model = new ModelAndView("admin/paymentLst");

        Pageable pageable = PageRequest.of(0, 20);
        Iterable<Payment> pLst = paymentDao.findALLByState(pageable).getContent();
        for (Payment p:pLst) {
            p.setValue(p.getValue().replaceAll(" ","\n"));
        }
        model.addObject("pLst", pLst);
        model.addObject("username", principal.getName());
        model.addObject("success", false);

        return model;
    }

    @RequestMapping("/admin/revealPaymentByParam")
    @ResponseBody
    public ModelAndView revealPaymentByParam(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/paymentLst");

        Iterable<Payment> pLst;
        Pageable pageable = PageRequest.of(0, 20);
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

        model.addObject("pLst", pLst);
        if(param==null)
            model.addObject("searchbar", "");
        else
            model.addObject("searchbar", param);
        model.addObject("username", principal.getName());

        return model;
    }

    //批量删除支付订单
    @RequestMapping("/admin/deletePayment")
    @ResponseBody
    public String deletePayment(String paymentid){

        try{
            List<String> ids = new ArrayList<>();
            if(paymentid.contains(","))
                ids = Arrays.asList(paymentid.split(","));
            else
                ids.add(paymentid);
            Iterable<Payment> payments = paymentDao.findAllById(ids);
            for (Payment p : payments) {
                Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(p.getValue().split(" ")));
                for (Orders o : orders) {
                    if(p.getType().getId() == Payment.PASTYPE_PRODUCT)
                        o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_PRODUCT_PAID).get());
                    else
                        o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_FERIGHT_PAID).get());
                }
                orderDao.saveAll(orders);
            }
            paymentDao.deleteAll(payments);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    //手动销账
    @RequestMapping("/admin/setPaymentTrue")
    @ResponseBody
    public String setPaymentTrue(String paymentid, String actualPrice){

        try{
            Payment payment = paymentDao.findById(paymentid).get();

            if(payment.getTotprice()<=Double.parseDouble(actualPrice)+payment.getActualprice()){
                payment.setActualprice(Double.parseDouble(actualPrice)+payment.getActualprice());
                Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(payment.getValue().split(" ")));
                //判断是货款还是运费
                if(payment.getType().getId()==Payment.PASTYPE_PRODUCT)
                    payment.setState(pastateDao.findById(Payment.PASTATE_PAID).get());
                else {
                    payment.setState(pastateDao.findById(orders.iterator().next().getProduct().getState().getId()-1).get());
                }
                if(payment.getType().getId()==0){
                    for (Orders o : orders)
                        o.setState(stateDao.findById(Orders.OSTATE_PRODUCT_PAID).get());
                }else{
                    for (Orders o : orders)
                        o.setState(stateDao.findById(Orders.OSTATE_FERIGHT_PAID).get());
                }
                payment.setTime(new Date());
                orderDao.saveAll(orders);
            }else{
                payment.setActualprice(Double.parseDouble(actualPrice)+payment.getActualprice());
                payment.setState(pastateDao.findById(Payment.PASTATE_PAYFAIL).get());
            }

            paymentDao.save(payment);
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

            for (Orders o : orders){
                //根据支付类别回滚订单状态
                if(p.getType().getId() == Payment.PASTYPE_PRODUCT)
                    o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_PRODUCT_PAID).get());
                else
                    o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_FERIGHT_PAID).get());
            }

            orderDao.saveAll(orders);
            p.setState(pastateDao.findById(Payment.PASTATE_UNPAID).get());
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
     * 自动销账
     * @param file
     */
    @RequestMapping(value = "/admin/uploadPayment", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView uploadPayment(@RequestParam("excelFile") MultipartFile file, Principal principal, HttpServletRequest request, HttpServletResponse response){
        ModelAndView model = new ModelAndView("admin/paymentLst");
        try {
            Map<String, Object> importResult = paymentService.paymentImport(file.getOriginalFilename(),file,response);
            List<Payment> list= (List<Payment>)importResult.get("paymentList");
            List<List<Object>> errorList= (List<List<Object>>)importResult.get("errorList");

            Pageable pageable = PageRequest.of(0, 20);
            Iterable<Payment> pLst = paymentDao.findALLByState(pageable).getContent();
            for (Payment p:pLst) {
                p.setValue(p.getValue().replaceAll(" ","\n"));
            }
            model.addObject("pLst", pLst);
            model.addObject("successCount", list.size());
            model.addObject("successLst", list.iterator());
            model.addObject("failCount", errorList.size());
            model.addObject("failLst", errorList);
            model.addObject("success", true);
            model.addObject("username", principal.getName());

            //num=sListService.doUploadFile(list);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入失败");
        }
        return model;
    }

    @RequestMapping(value = "/admin/exportErrorPayments", method = RequestMethod.GET)
    @ResponseBody
    public void exportErrorOrders(@RequestParam List<String> errorList, HttpServletResponse response) throws ParseException {
        List<String[]> data = new ArrayList<>();
        for (String e : errorList) {
            String[] values;
            try{
                values = e.split("\\[")[1].split("]")[0].split(", ");
            }catch(Exception ex){
                System.out.println("=======errorMsg======" + ex.getMessage());
                values = new String[3];
                values[0] = errorList.get(0).split("\\[")[1];
                values[1] = errorList.get(1);
                values[2] = errorList.get(2).split("]")[0];
                data.clear();
                data.add(values);
                break;
            }
            data.add(values);
        }
        paymentService.exportErrorPayments(data, response);
    }

    @RequestMapping("/admin/downloadPaymentTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response){

        paymentService.exportTemplate(request,response);

    }

}
