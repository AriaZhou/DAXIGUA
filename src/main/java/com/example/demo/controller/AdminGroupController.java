package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class AdminGroupController {

    @Autowired
    UserDAO userDao;
    @Autowired
    ProductDAO productDao;
    @Autowired
    GroupDAO groupDao;
    @Autowired
    PStateDAO pstateDao;

    @RequestMapping("/admin/revealAllGroup")
    @ResponseBody
    public ModelAndView revealAllGroup(Principal principal) {

        ModelAndView model = new ModelAndView("admin/groupLst");

        Pageable pageable = PageRequest.of(0, 20);
        Iterable<Group> gLst = groupDao.findALLByState(pageable);
        model.addObject("gLst", gLst);
        model.addObject("username", principal.getName());

        return model;
    }

    @RequestMapping("/admin/revealGroupByParam")
    @ResponseBody
    public ModelAndView revealGroupByParam(Principal principal, String param) {

        ModelAndView model = new ModelAndView("admin/groupLst");

        Iterable<Group> gLst;
        Pageable pageable = PageRequest.of(0, 20);
        if(param==null)
            gLst = groupDao.findALLByState(pageable).getContent();
        else{
            String[] params = param.split(" ");
            String formattedParams = "";
            for (int i = 0; i < params.length; i++) {
                formattedParams += params[i];
                if(i != params.length-1)
                    formattedParams += "&";
            }
            gLst = groupDao.findALLByStateAndParam("%"+formattedParams+"%", pageable).getContent();
        }

        model.addObject("gLst", gLst);
        if(param==null)
            model.addObject("searchbar", "");
        else
            model.addObject("searchbar", param);
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

    //批量删除团
    @RequestMapping("/admin/deleteGroup")
    @ResponseBody
    public String deleteGroup(String groupid){

        try{
            List<String> ids = new ArrayList<>();
            if(groupid.contains(","))
                ids = Arrays.asList(groupid.split(","));
            else
                ids.add(groupid);

            Iterable<Group> groups = groupDao.findAllById(ids);
            for (Group g: groups) {
                productDao.deleteAll(g.getProducts());
            }
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
            Group gg = groupDao.findById(g.getId()).get();
            //时间推后的话产品状态要跟着改
            if(gg.getEndtime().getTime() < new Date().getTime() && g.getEndtime().getTime() > new Date().getTime()){
                for(Product p : gg.getProducts()){
                    if(p.getPcount()>0){
                        p.setState(pstateDao.findById((long)0).get());
                        productDao.save(p);
                    }
                }
            }
            groupDao.save(g);
            return "1";
        }catch (Exception e){
            System.out.println("----error----");
            System.out.println(e.getMessage());
            System.out.println("----error----");
            return "0";
        }

    }

    @RequestMapping("/admin/getGroupList")
    @ResponseBody
    public List<String> getGroupList() {
        Iterable<Group> gLst = groupDao.findAll();
        List<String> gidLst = new ArrayList<>();

        for(Group g : gLst){
            gidLst.add(g.getId());
        }

        return gidLst;

    }

}
