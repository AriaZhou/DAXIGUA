package com.example.demo;

import com.example.demo.entity.Payment;
import com.example.demo.util.ImportExcelUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

public class UploadUtil {
    public static List<Payment> doUploadFile(MultipartFile file, HttpServletRequest request) throws Exception {

        //        if (!file.isEmpty()) {
        //            try {
        //                // 这里将上传得到的文件保存指定目录下
        //                FileUtils.copyInputStreamToFile(file.getInputStream(),
        //                        new File("d:\\upload\\file\\", System.currentTimeMillis() + file.getOriginalFilename()));
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }
        InputStream in = null;
        List<List<Object>> listob = null;
        in = file.getInputStream();
        List<Payment> list=new ImportExcelUtil().getPaymentListByExcel(in, file.getOriginalFilename());

        return list;
    }
}
