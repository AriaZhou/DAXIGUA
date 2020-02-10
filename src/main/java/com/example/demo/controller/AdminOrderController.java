package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.apache.poi.ss.usermodel.Row;
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
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AdminOrderController {

    @Autowired
    ProductDAO productDao;
    @Autowired
    GroupDAO groupDao;
    @Autowired
    OrderDAO orderDao;
    @Autowired
    StateDAO stateDao;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/admin/revealAllOrder")
    @ResponseBody
    public ModelAndView revealAllOrder(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/orderLst");

        Pageable pageable = PageRequest.of(0, 20);
        Iterable<Orders> oLst = orderDao.findALLByState(pageable);

        model.addObject("oLst", oLst);
        Iterable<Group> gLst = groupDao.findAllWithNowTimeBefore(new Date());
        model.addObject("gLst", gLst);
        Iterable<State> sLst = stateDao.findAll();
        model.addObject("sLst", sLst);
        model.addObject("success", false);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/revealOrderByParam")
    @ResponseBody
    public ModelAndView revealOrderByParam(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/orderLst");

        Iterable<Orders> oLst;
        Pageable pageable = PageRequest.of(0, 20);
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

        model.addObject("oLst", oLst);
        Iterable<Group> gLst = groupDao.findAllWithNowTimeBefore(new Date());
        model.addObject("gLst", gLst);
        Iterable<State> sLst = stateDao.findAll();
        model.addObject("sLst", sLst);
        if(param==null)
            model.addObject("searchbar", "");
        else
            model.addObject("searchbar", param);
        model.addObject("username", principal.getName());

        return model;
    }

//    @RequestMapping("/admin/loadMoreOrder")
//    @ResponseBody
//    public Iterable<Orders> loadMoreOrder(String param, int page) {
//
//        Iterable<Orders> oLst;
//        Pageable pageable = PageRequest.of(page, 20);
//        if(param==null)
//            oLst = orderDao.findALLByState(pageable).getContent();
//        else{
//            String[] params = param.split(" ");
//            String formattedParams = "";
//            for (int i = 0; i < params.length; i++) {
//                formattedParams += params[i];
//                if(i != params.length-1)
//                    formattedParams += "&";
//            }
//            oLst = orderDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
//        }
//        return oLst;
//    }

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

    //批量删除订单
    @RequestMapping("/admin/deleteOrder")
    @ResponseBody
    public String deleteOrder(String orderid){

        try{
            List<String> ids = new ArrayList<>();
            if(orderid.contains(","))
                ids = Arrays.asList(orderid.split(","));
            else
                ids.add(orderid);

            Iterable<Orders> oLst = orderDao.findAllById(ids);
            List<Product> pLst = new ArrayList<>();
            for (Orders o:oLst) {
                Product p = productDao.findProductForUpdate(o.getProduct().getId());
                p.setPcount(p.getPcount()+o.getOcount());
                p.setOrdercount(p.getOrdercount()-o.getOcount());
                pLst.add(p);
            }
            productDao.saveAll(pLst);

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
    public String modifyOrder(@ModelAttribute Orders o) {

        try{
            Orders ot = orderDao.findById(o.getId()).get();

            Product p = productDao.findProductForUpdate(o.getProduct().getId());
            p.setPcount(p.getPcount()+ot.getOcount()-o.getOcount());
            p.setOrdercount(p.getOrdercount()-ot.getOcount()+o.getOcount());
            productDao.save(p);

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

    //(批量)修改订单状态
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

    //导出订单表单
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

    //导出全部订单表单
    @RequestMapping("/admin/exportData")
    public void exportData(HttpServletResponse response){
        orderService.export(null,response);
    }

    /**
     * 数据上传导入
     * 自动销账
     * @param file
     */
    @RequestMapping(value = "/admin/uploadOrders", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView uploadrders(@RequestParam("excelFile") MultipartFile file, Principal principal, HttpServletRequest request, HttpServletResponse response){
        ModelAndView model = new ModelAndView("admin/orderLst");
        try {
            Map<String, Object> importResult = orderService.orderImport(file.getOriginalFilename(),file,response);
            List<Orders> list= (List<Orders>)importResult.get("orderList");
            List<List<Object>> errorList= (List<List<Object>>)importResult.get("errorList");

            Pageable pageable = PageRequest.of(0, 20);
            Iterable<Orders> oLst = orderDao.findALLByState(pageable);

            model.addObject("oLst", oLst);
            Iterable<Group> gLst = groupDao.findAllWithNowTimeBefore(new Date());
            model.addObject("gLst", gLst);
            Iterable<State> sLst = stateDao.findAll();
            model.addObject("sLst", sLst);
            model.addObject("successCount", list.size());
            model.addObject("successOLst", list.iterator());
            model.addObject("failCount", errorList.size());
            model.addObject("failOLst", errorList);
            model.addObject("success", true);
            model.addObject("username", principal.getName());

            //num=sListService.doUploadFile(list);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入失败");
        }
        return model;
    }

    @RequestMapping(value = "/admin/exportErrorOrders", method = RequestMethod.GET)
    @ResponseBody
    public void exportErrorOrders(@RequestParam List<String> errorList, HttpServletResponse response){
        List<String[]> data = new ArrayList<>();
        for (String e : errorList) {
            String[] values;
            try{
                values = e.split("\\[")[1].split("]")[0].split(", ");
            }catch(Exception ex){
                System.out.println("=======errorMsg======" + ex.getMessage());
                values = new String[4];
                values[0] = errorList.get(0).split("\\[")[1];
                values[1] = errorList.get(1);
                values[2] = errorList.get(2);
                values[3] = errorList.get(3).split("]")[0];
                data.clear();
                data.add(values);
                break;
            }
            data.add(values);
        }
        orderService.exportErrorOrders(data, response);
    }

    @RequestMapping("/admin/downloadOrderTemplate")
    @ResponseBody
    public void downloadOrderTemplate(HttpServletRequest request, HttpServletResponse response){

        orderService.exportTemplate(request,response);

    }

}
