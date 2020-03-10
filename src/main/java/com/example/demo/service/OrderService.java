package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    OrderDAO orderDao;
    @Autowired
    StateDAO stateDao;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;  //注入bcryct加密

    public Map<String, Object> orderImport(String fileName, MultipartFile file, HttpServletResponse response) throws Exception {

        List<Orders> orderList = new ArrayList<>();
        List<List<Object>> errorList = new ArrayList<>();

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }

            String uid = checkExcelValueType(row.getCell(0));

            try{
                User user;
                if(userDao.existsById(uid))
                    user = userDao.findById(uid).get();
                else{
                    user = new User();
                    user.setUsername(uid);
                    user.setUname(checkExcelValueType(row.getCell(1)));
                    user.setPassword(bCryptPasswordEncoder.encode(uid));
                    user.setRole("ROLE_USER");
                    user.setPhone("null");
                    userDao.save(user);

//                    List<Object> values = new ArrayList<>();
//                    values.add(uid);
//                    values.add(checkExcelValueType(row.getCell(1)));
//                    values.add(checkExcelValueType(row.getCell(2)));
//                    values.add("QQ号不正确，用户不存在");
//                    errorList.add(values);
//                    continue;
                }
                String[] orders = checkExcelValueType(row.getCell(2)).split("; ");
                for (int i = 0; i < orders.length; i++) {
                    String[] values = orders[i].split("\\*");
                    Product product = productDao.findByName(values[0]);
                    Orders o = new Orders();
                    o.setId(new Date().getTime()+"");
                    o.setUser(user);
                    o.setState(stateDao.findById(0L).get());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    o.setTime(sdf.format(new Date()));
                    o.setOcount(Integer.parseInt(values[1]));
                    o.setPrice(o.getOcount()*Double.parseDouble(product.getPrice())+"");
                    product.setOrdercount(product.getOrdercount()+Integer.parseInt(values[1]));
                    product.setPcount(product.getPcount()-Integer.parseInt(values[1]));
                    productDao.save(product);
                    o.setProduct(product);
                    orderList.add(o);
                }
            }catch (Exception e){
                List<Object> values = new ArrayList<>();
                values.add(uid);
                values.add(checkExcelValueType(row.getCell(1)));
                values.add(checkExcelValueType(row.getCell(2)));
//                values.add(row.getCell(3).getNumericCellValue());
                values.add("格式有误");
                errorList.add(values);
            }

        }
        orderDao.saveAll(orderList);
//        if(!errorList.isEmpty())
//            exportErrorOrders(errorList,response);

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("orderList", orderList);
        returnData.put("errorList", errorList);
        return returnData;
    }

    public void exportErrorOrders(List<String[]> errorList, HttpServletResponse response){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("错误订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("QQ号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("群昵称");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单");
//        cell.setCellStyle(style);
//        cell = row.createCell(3);
//        cell.setCellValue("小计");
//        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("错误标记");
//        cell.setCellStyle(style);

        int i = 1;
        for (String[] e : errorList) {
            row = sheet.createRow(i);

            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(Integer.parseInt(e[0]));
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[1]);
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[2]);
//            cell2 = row.createCell(3);
//            cell2.setCellStyle(style);
//            cell2.setCellValue(Double.parseDouble(e[3]));
            cell2 = row.createCell(3);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[3]);
            i++;
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("order-import-fail" + ".xls").getBytes(), "utf-8"));
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

    public void exportTemplate(HttpServletRequest request, HttpServletResponse response){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("支付订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("QQ号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("群昵称");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单");
//        cell.setCellStyle(style);
//        cell = row.createCell(3);
//        cell.setCellValue("小计");
//        cell.setCellStyle(style);

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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("order-template" + ".xls").getBytes(), "utf-8"));
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

    public void addOrder(String pId, int count, Principal principal){
        Orders o = new Orders();
        Product p = productDao.findProductForUpdate(pId);
        o.setUser(userDao.findById(principal.getName()).get());
        o.setOcount(count);
        o.setProduct(p);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        o.setTime(format.format(now));
        String oId = "o"+now.getTime();
        while(orderDao.existsById(oId))
            oId = "o"+now.getTime();
        o.setId(oId);
        o.setState(stateDao.findById(Orders.OSTATE_UNPAID).get());
        o.setPrice(Integer.parseInt(productDao.findById(pId).get().getPrice())*count+"");
        orderDao.save(o);
        p.setPcount(p.getPcount()-count);
        p.setOrdercount(p.getOrdercount()+count);
        productDao.save(p);
    }

    public void addOrder(Orders o){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        o.setTime(format.format(now));
        String oId = "o"+now.getTime();
        while(orderDao.existsById(oId))
            oId = "o"+now.getTime();
        o.setId(oId);
        Product p = productDao.findProductForUpdate(o.getProduct().getId());
        p.setPcount(p.getPcount()-o.getOcount());
        p.setOrdercount(p.getOrdercount()+o.getOcount());
        productDao.save(p);
        orderDao.save(o);
    }

    public String checkExcelValueType(Cell cell){
        try{
            if(cell.getCellType().toString().equals("NUMERIC"))
                return  (int)cell.getNumericCellValue()+"";
            else
                return  cell.getStringCellValue();
        }catch (Exception e){
            System.out.println(e.getMessage()+"error============");
            return null;
        }
    }

    /**
     * 导出数据保存成Excel
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    public void export(List<String> ids, HttpServletResponse response) {
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
            //           cell2.setCellStyle(lockstyle);
            cell2.setCellValue(o.getState().getId()+"-"+o.getState().getValue());
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

        //下拉选单
        // 准备下拉列表数据
        int index = 0;
        String[] strs = new String[8];
        for (State s : stateDao.findAll()){
            strs[index]=s.getId()+"-"+s.getValue();
            index++;
        }
        // 设置第一列的1-10行为下拉列表
        CellRangeAddressList regions = new CellRangeAddressList(1, i, 5, 5);
        // 创建下拉列表数据
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(strs);
        // 绑定
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);

        HSSFCellStyle lockstyle = wb.createCellStyle();
        lockstyle.setLocked(true);
        sheet.setDefaultColumnStyle(5, lockstyle);

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

}
