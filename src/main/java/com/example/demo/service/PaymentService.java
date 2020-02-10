package com.example.demo.service;

import com.example.demo.dao.*;
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
import java.math.BigInteger;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    StateDAO stateDao;

    @Autowired
    PaStateDAO pastateDao;

    @Autowired
    PTypeDAO pTypeDAO;

    @Autowired
    OrderDAO orderDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    PaymentDAO paymentDao;


    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public Map<String, Object> paymentImport(String fileName, MultipartFile file, HttpServletResponse response) throws Exception {

        boolean notNull = false;
        List<Payment> paymentList = new ArrayList<>();
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
            Date payTime = row.getCell(2).getDateCellValue();

            try{
                Payment payment = paymentDAO.findById(pid).get();
                if(payment.getId().equals(pid)&&payment.getTotprice()<=actualprice+payment.getActualprice()){
                    payment.setActualprice(actualprice+payment.getActualprice());
                    Iterable<Orders> orders = orderDAO.findAllById(Arrays.asList(payment.getValue().split(" ")));
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
                    payment.setTime(payTime);
                    orderDAO.saveAll(orders);
                }else{
                    payment.setActualprice(actualprice+payment.getActualprice());
                    payment.setState(pastateDao.findById(Payment.PASTATE_PAYFAIL).get());
                }
                paymentList.add(payment);
            }catch (Exception e){
                List<Object> values = new ArrayList<>();
                values.add(pid);
                values.add(actualprice);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                values.add(sdf.format(payTime));
                errorList.add(values);
            }

        }
        paymentDAO.saveAll(paymentList);
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("paymentList", paymentList);
        returnData.put("errorList", errorList);

        return returnData;
    }

    public void exportErrorPayments(List<String[]> errorList, HttpServletResponse response) throws ParseException {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("错误支付订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("备注编号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("价格");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("支付时间");
//        cell.setCellStyle(style);

        int i = 1;
        for (String[] e : errorList) {
            row = sheet.createRow(i);

            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[0]);
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[1]);
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cell2.setCellValue(sdf.parse(e[2]));
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("payment-import-fail" + ".xls").getBytes(), "utf-8"));
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
        cell = row.createCell(2);
        cell.setCellValue("支付时间");
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

    public String addPayment(String ids, double price, Principal principal){
        StringBuilder remark = new StringBuilder();
        while(orderDao.existsById(remark.toString()) || remark.length()==0){
            long id = Long.parseLong(String.valueOf(new Date().getTime()).substring(2));
            while(id>0) {
                long tmp = id%62;
                if (tmp < 10)
                    remark.append(tmp);
                else if (tmp < 36)
                    remark.append((char) (tmp + 87));
                else
                    remark.append((char) (tmp + 29));
                id = id/62;
            }
        }
        Payment p = new Payment();
        p.setId(remark.toString());
        p.setTotprice(price);
        p.setState(pastateDao.findById(Payment.PASTATE_UNPAID).get());
        p.setActualprice(0);
        p.setType(pTypeDAO.findById(Payment.PASTYPE_PRODUCT).get());
        p.setValue(ids);

        Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(ids.split(" ")));

        for (Orders o : orders)
            o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_PRODUCT_PAID).get());

        orderDao.saveAll(orders);

        p.setUser(userDao.findById(principal.getName()).get());
        paymentDao.save(p);

        return remark.toString();
    }

    public String addFreightPayment(int type, String ids, Principal principal){
        BigInteger id = new BigInteger(String.valueOf(new Date().getTime()).substring(2)+""+Integer.parseInt(principal.getName().substring(2,3)));
        BigInteger zero = new BigInteger("0");
        BigInteger divider = new BigInteger("62");
        StringBuilder remark = new StringBuilder();
        while(id.compareTo(zero) == 1) {
            long tmp = id.remainder(divider).longValue();
            if (tmp < 10)
                remark.append(tmp);
            else if (tmp < 36)
                remark.append((char) (tmp + 87));
            else
                remark.append((char) (tmp + 29));
            id = id.divide(divider);
        }
        Payment p = new Payment();
        p.setId(remark.toString());

        switch (userDao.findById(principal.getName()).get().getProvince()){
            case "广东省": p.setTotprice(10);break;
            case "上海市": p.setTotprice(12);break;
            case "浙江省": p.setTotprice(12);break;
            case "安徽省": p.setTotprice(12);break;
            case "江西省": p.setTotprice(12);break;
            case "福建省": p.setTotprice(12);break;
            case "山东省": p.setTotprice(15);break;
            case "广西省": p.setTotprice(12);break;
            case "海南省": p.setTotprice(15);break;
            case "湖北省": p.setTotprice(12);break;
            case "湖南省": p.setTotprice(12);break;
            case "河南省": p.setTotprice(15);break;
            case "北京市": p.setTotprice(15);break;
            case "天津市": p.setTotprice(15);break;
            case "河北省": p.setTotprice(15);break;
            case "山西省": p.setTotprice(15);break;
            case "内蒙古自治区": p.setTotprice(20);break;
            case "辽宁省": p.setTotprice(18);break;
            case "吉林省": p.setTotprice(18);break;
            case "黑龙江省": p.setTotprice(18);break;
            case "四川省": p.setTotprice(15);break;
            case "重庆市": p.setTotprice(15);break;
            case "贵州省": p.setTotprice(15);break;
            case "云南省": p.setTotprice(15);break;
            case "西藏自治区": p.setTotprice(20);break;
            case "陕西省": p.setTotprice(15);break;
            case "甘肃省": p.setTotprice(20);break;
            case "青海省": p.setTotprice(20);break;
            case "宁夏自治区": p.setTotprice(20);break;
            case "新疆自治区": p.setTotprice(20);break;
            default: p.setTotprice(20);break;
        }

        p.setType(pTypeDAO.findById((long)type).get());
        p.setState(pastateDao.findById(Payment.PASTATE_UNPAID).get());
        p.setActualprice(0);
        p.setValue(ids);

        Iterable<Orders> orders = orderDao.findAllById(Arrays.asList(ids.split(" ")));

        for (Orders o : orders)
            o.setState(stateDao.findById(Orders.OSTATE_SUBMIT_FERIGHT_PAID).get());

        orderDao.saveAll(orders);

        p.setUser(userDao.findById(principal.getName()).get());

        paymentDao.save(p);
        return p.getTotprice()+" "+remark.toString();
    }

}
