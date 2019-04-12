package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    List<VoBean> list = new ArrayList<>();


    @RequestMapping("/")
    public String testcase01(Model model, HttpServletRequest request,HttpSession session){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        List<VoBean> newsList = userService.queryAllNews();
        model.addAttribute("vos",newsList);
        return "home";
    }

    //用户注册
    @RequestMapping("/reg")
    @ResponseBody
    public Map register(@RequestParam String username, String password,HttpSession session){
        //首先判断用户名是否被占用,如果查询结果为空则正常进行插入
        User user = userDao.queryUserIsExistByUsername(username);
        Map<Object, Object> hashMap = new HashMap<>();
        if (user == null) {
            userDao.insertUser(username, password);
            User user2 = userDao.queryUserIsExistByUsername(username);
            hashMap.put("code", 0);
            session.setAttribute("user",user2);
            return hashMap;
        }else {
            hashMap.put("code", 1);
            hashMap.put("msgname","用户名已经被注册");
            return hashMap;
        }
    }

    //用户登陆
    @RequestMapping("login")
    @ResponseBody
    public Map login(@RequestParam String username, String password, HttpSession session){
        //查询用户名和密码
        User user = userDao.queryUserByUsernameAndPassword(username,password);
        Map<Object, Object> hashMap = new HashMap<>();
        if (user == null) {
            hashMap.put("code", 1);
            hashMap.put("msgname","用户名或密码不正确");
            return hashMap;
        }else {
            hashMap.put("code", 0);
            session.setAttribute("user",user);
            List<VoBean> list = new ArrayList<>();
            session.setAttribute("vos",list);
            return hashMap;
        }
    }

    //用户注销
    @RequestMapping("logout")
    public String logout(HttpSession session,HttpServletRequest request,Model model){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        session.removeAttribute("user");
        return "redirect:/";
    }
    //用户信息查看
    @RequestMapping("user/{id}")
    public String userMess(@PathVariable("id") String id,HttpServletRequest request,Model model){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        System.out.println(id);
        User user = userService.queryUserById(id);
        model.addAttribute("user",user);
        return "personal";
    }
}
