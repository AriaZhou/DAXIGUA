package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.PState;
import com.example.demo.entity.Product;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductDAO productDao;
    @Autowired
    PStateDAO pstateDao;

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
