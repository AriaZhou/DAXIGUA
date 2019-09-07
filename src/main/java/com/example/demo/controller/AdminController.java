package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    StateDAO stateDao;

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
        Iterable<State> sLst = stateDao.findAll();
        model.addObject("sLst", sLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/addOrder")
    @ResponseBody
    public String addOrder(@ModelAttribute Orders o) {

        try{
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            o.setTime(format.format(now));
            o.setId(""+now.getTime());
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
            this.export(ids,response);
        }else{
            List<String> ids = new ArrayList<>();
            ids.add(tempIds);
            this.export(ids,response);
        }

    }

    @RequestMapping("/admin/exportData")
    public void exportData(HttpServletResponse response){
        this.export(null,response);
    }

    /**
     * 导出数据保存成Excel
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    private void export(List<String> ids, HttpServletResponse response) {
        Iterable<Orders> list= null;
        if(null == ids){
            list = orderDao.findAll();
        }else{
            list = orderDao.findAllById(ids);
        }

        String fileName = new Date().getTime()+"";

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

//        HSSFFont font = wb.createFont();
//        font.setFontName("宋体"); //字体
//        font.setBold(HSSFFont.BOLDWEIGHT_BOLD); //宽度

        // 第四步，创建单元格，并设置值表头 设置表头居中
//        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        style.setFont(font);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("订单号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("团号");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("名称");
//        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("数量");
//        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("价格");
//        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("状态");
//        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("QQ");
//        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("昵称");
//        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("地址");
//        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("电话");
//        cell.setCellStyle(style);

        // 第五步，写入实体数据
        int i = 0;
        for (Orders o : list)
        {
            row = sheet.createRow(i + 1);
            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getId());
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getProduct().getGroup().getId());
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getProduct().getPname());
            cell2 = row.createCell(3);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getOcount());
            cell2 = row.createCell(4);
//            cell2.setCellStyle(style);
            cell2.setCellValue(Double.parseDouble(o.getPrice()));
            cell2 = row.createCell(5);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getState().getId()+" - "+o.getState().getValue());
            cell2 = row.createCell(6);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getUser().getUsername());
            cell2 = row.createCell(7);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getUser().getUname());
            cell2 = row.createCell(8);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getUser().getAddress());
            cell2 = row.createCell(9);
//            cell2.setCellStyle(style);
            cell2.setCellValue(o.getUser().getPhone());
            i++;
        }
        //设置自动调整宽度
        for (int j = 0; j < 9; j++) {
            sheet.autoSizeColumn(j);
        }
        // 第六步，保存文件
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "utf-8"));
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("=====export exception=====\n"+ e);
        }finally {
            try {
                if(bis != null)
                    bis.close();
                if(bos != null)
                    bos.close();
            } catch (IOException e) {
                System.out.println("=====export exception=====\n"+ e);
            }
        }
    }

    @RequestMapping("/admin/revealAllUser")
    @ResponseBody
    public ModelAndView revealAllUser(Principal principal) {

        ModelAndView model = new ModelAndView("admin/userLst");

        Iterable<User> uLst = userDao.findAll();
        for (User u:uLst) {
            u.setPassword(u.getPassword().split("}")[1]);
        }
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
            u.setPassword("{noop}"+u.getPassword());
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
            System.out.println(u.getPassword());
            u.setPassword("{noop}"+u.getPassword());
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

}
