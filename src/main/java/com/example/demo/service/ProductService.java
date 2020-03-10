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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    GroupDAO groupDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    PStateDAO pstateDao;

    public Map<String, Object> productImport(String fileName, MultipartFile file, Principal principal) throws Exception {

        List<Product> productList = new ArrayList<>();
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

            String gid = checkExcelValueType(row.getCell(0));

            try{
                Group group;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                if(groupDao.existsById(gid))
                    group = groupDao.findById(gid).get();
                else{
                    group = new Group();
                    group.setId(gid);
                    group.setStarttime(sdf.parse("1900/01/01 00:00:00"));
                    group.setEndtime(sdf.parse("2900/01/01 00:00:00"));
                    groupDao.save(group);
                }

                Product product = new Product();
                product.setGroup(group);
                product.setId(checkExcelValueType(row.getCell(1)));
                product.setPcount(999);
                product.setOrdercount(0);
                product.setState(pstateDao.findById(0L).get());
                product.setUploadTime(sdf.format(new Date()));
                product.setUsername(principal.getName());
                product.setEnabled(1);
                product.setPname(checkExcelValueType(row.getCell(2)));
                product.setPrice(checkExcelValueType(row.getCell(3)));
                productList.add(product);
            }catch (Exception e){
                List<Object> values = new ArrayList<>();
                values.add(gid);
                values.add(checkExcelValueType(row.getCell(1)));
                values.add(checkExcelValueType(row.getCell(2)));
                values.add(checkExcelValueType(row.getCell(3)));
                values.add("格式有误");
                errorList.add(values);
            }

        }
        productDao.saveAll(productList);

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("productList", productList);
        returnData.put("errorList", errorList);
        return returnData;
    }

    public void exportErrorProducts(List<String[]> errorList, HttpServletResponse response){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("错误订单记录");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("团号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("墨水编号");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("墨水名");
//        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("单价");
//        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("错误标记");
//        cell.setCellStyle(style);

        int i = 1;
        for (String[] e : errorList) {
            row = sheet.createRow(i);

            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[0]+"");
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[1]+"");
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[2]);
            cell2 = row.createCell(3);
//            cell2.setCellStyle(style);
            cell2.setCellValue(Double.parseDouble(e[3]));
            cell2 = row.createCell(4);
//            cell2.setCellStyle(style);
            cell2.setCellValue(e[4]);
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("product-import-fail" + ".xls").getBytes(), "utf-8"));
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
        cell.setCellValue("团号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("墨水编号");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("墨水名");
//        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("单价");
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
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("product-template" + ".xls").getBytes(), "utf-8"));
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

    public String checkExcelValueType(Cell cell){
        try{
            if(cell.getCellType().toString().equals("NUMERIC"))
                return  (int)cell.getNumericCellValue()+"";
            else
                return  cell.getStringCellValue();
        }catch (Exception e){
            System.out.println("=====export exception=====\n"+ e);
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
    public void exportProduct(List<String> ids, HttpServletResponse response) {
        Iterable<Product> list= null;
        if(null == ids){
            list = productDao.findAll();
        }else{
            list = productDao.findAllById(ids);
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
        cell.setCellValue("商品编号");
//        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("团号");
//        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("名称");
//        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("价格");
//        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("订购数量");
//        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("货物状态");
//        cell.setCellStyle(style);

        // 第五步，写入实体数据
        int i = 0;
        for (Product p : list)
        {
            row = sheet.createRow(i + 1);
            // 第四步，创建单元格，并设置值
            HSSFCell cell2 = row.createCell(0);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getId());
            cell2 = row.createCell(1);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getGroup().getId());
            cell2 = row.createCell(2);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getPname());
            cell2 = row.createCell(3);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getPrice());
            cell2 = row.createCell(4);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getOrdercount());
            cell2 = row.createCell(5);
//            cell2.setCellStyle(style);
            cell2.setCellValue(p.getState().getId()+"-"+p.getState().getValue());
            i++;
        }
        //设置自动调整宽度
        for (int j = 0; j < 6; j++) {
            sheet.autoSizeColumn(j);
        }

        //下拉选单
        // 准备下拉列表数据
        int index = 0;
        String[] strs = new String[6];
        for (PState s : pstateDao.findAll()){
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
