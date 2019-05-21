package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.*;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.JedisUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
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
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;
    List<VoBean> list = new ArrayList<>();

    @RequestMapping("/")
    public String testcase01(Model model, HttpServletRequest request, HttpSession session, @Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNews();

        List<VoBean> newsListbypage = userService.queryAllNewsByPage((i-1)*10,10);
        System.out.println((i-1)*10);

        User user = (User) session.getAttribute("user");
        Jedis jedis = JedisUtils.getJedisFromPool();
        //这时user已经登陆了
        if (user!=null){
            //模糊查询所有与新闻相关的key
            Set<String> userset = jedis.keys("usersetlike*");
            for (String key : userset) {
                //查询该id是否存在于其中
                //如果存在就返回like值为1
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                if (sismember) {
                for (VoBean vo : newsListbypage
                ) {
                    //这个摘录出来新闻的id
                    String s = key.substring(11);
                        int i2 = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i2) {
                            vo.setLike(1);
                        }
                    }
                }
            }
            Set<String> userset2 = jedis.keys("usersetdislike*");
            for (String key : userset2) {
                //查询该id是否存在于其中
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                //如果存在就返回like值为1
                if (sismember) {
                    for (VoBean vo : newsListbypage
                    ) {
                        String s = key.substring(14);
                        int i3 = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i3) {
                            vo.setLike(-1);
                        }
                    }
                }
            }
        }

        for (VoBean vo : newsList
        ) {
            Date createdDate1 = vo.getNews().getCreatedDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format = sdf.format(createdDate1);
            vo.getNews().setTime(format);

            int likeCount = vo.getNews().getLikeCount();
            int commentCount = vo.getNews().getCommentCount();
            Date createdDate = vo.getNews().getCreatedDate();
            Date date = new Date();
            long time_cre = createdDate.getTime();
            long time_now = date.getTime();
            double score = 0;
            long l = (time_now - time_cre) / (1000 * 3600);

            if ((time_now-time_cre)/(1000*3600) >= 1) {
                score =
                        ((likeCount - 1) + (commentCount - 1)) + 1
                                / Math.pow((time_now - time_cre) / (1000 * 3600), 1.8);
            }else {
                score = ((likeCount - 1) + (commentCount - 1)) + 1 / Math.pow(1, 1.8);
                }
            vo.getNews().setScore((float)score);
            userService.updateNewsScore(vo.getNews().getId(),score);
        }

        for (VoBean vo:newsListbypage
             ) {
            Date createdDate = vo.getNews().getCreatedDate();
            System.out.println("c:"+createdDate);
            long time = createdDate.getTime();
            System.out.println("time:"+time);
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;

            double l = test/(1000.0*3600.0);
            long round = Math.round(l);

            if (round <= 0){
                vo.getNews().setTime("刚刚");
            }else if(round>0&&round<=24){
                vo.getNews().setTime(round+"个小时前");
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(createdDate);
                vo.getNews().setTime(""+format);
            }
        }

        model.addAttribute("vos",newsListbypage);
        model.addAttribute("currentPage",i);
        PageBean pageBean = new PageBean<>();
        pageBean.setTotalPage(newsList.size() % 10 == 0 ?  newsList.size()/10 : newsList.size()/10 +1);
        model.addAttribute("newMessPage",pageBean);
        model.addAttribute("size",newsList.size());

        jedis.close();
        return "home";
    }


    @RequestMapping("/myshare")
    public String testcase05(Model model, HttpServletRequest request,HttpSession session){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        User user = (User) session.getAttribute("user");
        List<VoBean> newsList = userService.queryAllNewsOfUserById(user.getId());
        Jedis jedis = JedisUtils.getJedisFromPool();
        //这时user已经登陆了
        if (user!=null){
            //模糊查询所有与新闻相关的key
            Set<String> userset = jedis.keys("usersetlike*");
            for (String key : userset) {
                //查询该id是否存在于其中
                //如果存在就返回like值为1
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        //这个摘录出来新闻的id
                        String s = key.substring(11);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(1);
                        }
                    }
                }
            }
            Set<String> userset2 = jedis.keys("usersetdislike*");
            for (String key : userset2) {
                //查询该id是否存在于其中
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                //如果存在就返回like值为1
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        String s = key.substring(14);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(-1);
                        }
                    }
                }
            }
        }
        String page = request.getParameter("page");
        System.out.println("page:"+page);
        model.addAttribute("vos",newsList);
        jedis.close();
        return "home";
    }

    @RequestMapping("/recentNews")
    public String testcase07(Model model, HttpServletRequest request,HttpSession session){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        User user = (User) session.getAttribute("user");
        List<VoBean> newsList = userService.queryAllNewsOfRecent();
        Jedis jedis = JedisUtils.getJedisFromPool();
        //这时user已经登陆了
        if (user!=null){
            //模糊查询所有与新闻相关的key
            Set<String> userset = jedis.keys("usersetlike*");
            for (String key : userset) {
                //查询该id是否存在于其中
                //如果存在就返回like值为1
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        //这个摘录出来新闻的id
                        String s = key.substring(11);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(1);
                        }
                    }
                }
            }
            Set<String> userset2 = jedis.keys("usersetdislike*");
            for (String key : userset2) {
                //查询该id是否存在于其中
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                //如果存在就返回like值为1
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        String s = key.substring(14);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(-1);
                        }
                    }
                }
            }
        }
        model.addAttribute("vos",newsList);
        jedis.close();
        return "recentNews";
    }


    static {
        ArrayList<String> headimage = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            headimage.add("http://localhost/images/"+i+".jpg");
        }
    }

    //用户注册
    @RequestMapping("/reg")
    @ResponseBody
    public Map register(@RequestParam String username, String password,HttpSession session){
        //首先判断用户名是否被占用,如果查询结果为空则正常进行插入
        User user = userDao.queryUserIsExistByUsername(username);
        Map<Object, Object> hashMap = new HashMap<>();
        if (user == null) {
            ArrayList<String> headimage = new ArrayList<>();
            for (int i = 1; i <= 15; i++) {
                headimage.add("http://localhost/images/"+i+".jpg");
            }
            System.out.println("headimage:"+headimage);
            Random random = new Random();
            int i = random.nextInt(16);
            System.out.println("index:"+i);
            userDao.insertUser(username, password,headimage.get(i));
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
    public String userMess(@PathVariable("id") String id01,HttpServletRequest request,Model model){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        User user = userService.queryUserById(id01);
        model.addAttribute("user",user);

        model.addAttribute("contextPath",contextPath);
        List<VoBean> newsList = userService.queryAllNewsOfUserById(user.getId());
        Jedis jedis = JedisUtils.getJedisFromPool();
        //这时user已经登陆了
        if (user!=null){
            //模糊查询所有与新闻相关的key
            Set<String> userset = jedis.keys("usersetlike*");
            for (String key : userset) {
                //查询该id是否存在于其中
                //如果存在就返回like值为1
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        //这个摘录出来新闻的id
                        String s = key.substring(11);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(1);
                        }
                    }
                }
            }
            Set<String> userset2 = jedis.keys("usersetdislike*");
            for (String key : userset2) {
                //查询该id是否存在于其中
                Boolean sismember = jedis.sismember(key, ""+user.getId());
                //如果存在就返回like值为1
                if (sismember) {
                    for (VoBean vo : newsList
                    ) {
                        String s = key.substring(14);
                        int i = Integer.parseInt(s);
                        int id = vo.getNews().getId();
                        if (id == i) {
                            vo.setLike(-1);
                        }
                    }
                }
            }
        }
        String page = request.getParameter("page");
        model.addAttribute("vos",newsList);
        jedis.close();

        return "personal";
    }

    @RequestMapping("user/reply")
    public void reply(HttpServletRequest request,Model model,HttpSession session){
        String touserid = request.getParameter("touserid");
        User user = (User) session.getAttribute("user");
        int id = user.getId();

    }
}
