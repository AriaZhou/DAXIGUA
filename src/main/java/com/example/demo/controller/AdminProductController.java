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
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AdminProductController {

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
    private ProductService productService;

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
                p.setState(pstateDao.findById(Product.PSTATE_OVERSALE).get());
                productDao.save(p);
            }
        }

//        Iterable<Orders> oLst = orderDao.findAll();
//        for(Orders o : oLst){
//            Product p = productDao.findById(o.getProduct().getId()).get();
//            p.setOrdercount(p.getOrdercount()+o.getOcount());
//            p.setPcount(p.getPcount()-o.getOcount());
//            productDao.save(p);
//        }

        Pageable pageable = PageRequest.of(0, 20);
        pLst=productDao.findALLByState(pageable);
        model.addObject("pLst", pLst);
        Iterable<Group> gLst = groupDao.findAll();
        model.addObject("gLst", gLst);
        Iterable<PState> sLst = pstateDao.findAll();
        model.addObject("sLst", sLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/revealProductByParam")
    @ResponseBody
    public ModelAndView revealProductByParam(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/productLst");

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

        Pageable pageable = PageRequest.of(0, 20);
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

        model.addObject("pLst", pLst);
        Iterable<Group> gLst = groupDao.findAll();
        model.addObject("gLst", gLst);
        Iterable<PState> sLst = pstateDao.findAll();
        model.addObject("sLst", sLst);
        if(param==null)
            model.addObject("searchbar", "");
        else
            model.addObject("searchbar", param);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/getProductListByGroup")
    @ResponseBody
    public List<String> getProductListByGroup(String id) {
        //Iterable<Product> pLst = productDao.findByGroupIdVali(id);
        Iterable<Product> pLst = productDao.findByGroupId(id);
        List<String> pNameLst = new ArrayList<>();

        for(Product p : pLst){
            pNameLst.add(p.getId()+" "+p.getPname()+" "+p.getPrice());
        }

        return pNameLst;

    }

    @RequestMapping("/admin/addProduct")
    @ResponseBody
    public String addProduct(@ModelAttribute Product p, Principal principal) {

        try{
            p.setUsername(principal.getName());
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            p.setUploadTime(format.format(now));
            p.setId("p"+now.getTime());
            productDao.save(p);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }
    }

    //批量删除货物
    @RequestMapping("/admin/deleteProduct")
    @ResponseBody
    public String deleteProduct(String productid){

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

    //(批量)修改货物状态
    @RequestMapping("/admin/modifyProductState")
    @ResponseBody
    public String modifyProductState(String id, Long state) {

        try{
            List<String> ids = new ArrayList<>();
            if(id.contains(","))
                ids = Arrays.asList(id.split(","));
            else
                ids.add(id);

            Iterable<Product> pLSt = productDao.findAllById(ids);
            for(Product p:pLSt){
                p.setState(pstateDao.findById(state).get());
            }
            productDao.saveAll(pLSt);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return  "0";
        }
    }

    //导出货物表单
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

    /**
     * 数据上传导入
     * 自动销账
     * @param file
     */
    @RequestMapping(value = "/admin/uploadProducts", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView uploadProducts(@RequestParam("excelFile") MultipartFile file, Principal principal, HttpServletRequest request, HttpServletResponse response){
        ModelAndView model = new ModelAndView("admin/productLst");
        try {
            Map<String, Object> importResult = productService.productImport(file.getOriginalFilename(),file,principal);
            List<Product> list= (List<Product>)importResult.get("productList");
            List<List<Object>> errorList= (List<List<Object>>)importResult.get("errorList");

            Pageable pageable = PageRequest.of(0, 20);
            Iterable<Product> pLst=productDao.findALLByState(pageable);
            model.addObject("pLst", pLst);
            Iterable<Group> gLst = groupDao.findAll();
            model.addObject("gLst", gLst);
            Iterable<PState> sLst = pstateDao.findAll();
            model.addObject("sLst", sLst);
            model.addObject("successCount", list.size());
            model.addObject("successPLst", list.iterator());
            model.addObject("failCount", errorList.size());
            model.addObject("failPLst", errorList);
            model.addObject("success", true);
            model.addObject("username", principal.getName());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导入失败");
        }
        return model;
    }

    @RequestMapping(value = "/admin/exportErrorProducts", method = RequestMethod.GET)
    @ResponseBody
    public void exportErrorProducts(@RequestParam List<String> errorList, HttpServletResponse response){
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
        productService.exportErrorProducts(data, response);
    }

    @RequestMapping("/admin/downloadProductTemplate")
    @ResponseBody
    public void downloadOrderTemplate(HttpServletRequest request, HttpServletResponse response) {
        productService.exportTemplate(request, response);
    }

    //导出全部货物表单
    @RequestMapping("/admin/exportProductData")
    public void exportProductData(HttpServletResponse response){
        productService.exportProduct(null,response);
    }

}
