package com.example.demo.service;

import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.PaymentDAO;
import com.example.demo.dao.StateDAO;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Product;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    StateDAO stateDao;


    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public List<Payment> paymentImport(String fileName, MultipartFile file, HttpServletResponse response) throws Exception {

        boolean notNull = false;
        List<Payment> paymentList = new ArrayList<>();
        Map<String,Double> errorList = new HashMap<>();

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
        if(sheet!=null){
            notNull = true;
        }

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }

            String pid;
            try{
                pid = row.getCell(0).getStringCellValue();
            }catch (Exception e){
                pid = null;
            }
            double actualprice = row.getCell(1).getNumericCellValue();

            try{
                Payment payment = paymentDAO.findById(pid).get();
                if(payment.getId().equals(pid)&&payment.getTotprice()<=actualprice){
                    payment.setActualprice(actualprice);
                    payment.setState(2);
                    Iterable<Orders> orders = orderDAO.findAllById(Arrays.asList(payment.getValue().split(" ")));

                    for (Orders o : orders)
                        o.setState(stateDao.findById((long)2).get());

                    orderDAO.saveAll(orders);
                }else{
                    payment.setActualprice(actualprice);
                    payment.setState(1);
                }
                paymentList.add(payment);
            }catch (Exception e){
                errorList.put(pid,actualprice);
            }

        }
        paymentDAO.saveAll(paymentList);
        if(!errorList.isEmpty())
            exportErrorPayments(errorList,response);
        return paymentList;
    }

    public void exportErrorPayments(Map<String,Double> errorList, HttpServletResponse response){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("支付订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("备注编号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("价格");
//        cell.setCellStyle(style);

        int i = 0;
        for (Map.Entry<String, Double> e : errorList.entrySet()) {
            row = sheet.createRow(i + 1);
            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e.getKey());
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e.getValue());
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("import-fail" + ".xls").getBytes(), "utf-8"));
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
        cell.setCellValue("备注编号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("价格");
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("payment-template" + ".xls").getBytes(), "utf-8"));
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
