package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.JedisUtils;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
public class PersonalController {
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @RequestMapping("personal/delete")
    public String userMess(HttpServletRequest request, Model model, HttpSession session) {
        String op = request.getParameter("op");
        //获取需要删除的新闻id
        int id2 = Integer.parseInt(op);
        userService.removeUserById(id2);
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        //获取当前用户id
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);

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
        model.addAttribute("vos",newsList);
        jedis.close();
        return "personal";
    }

    @RequestMapping("personal/update")
    public String test02(HttpServletRequest request, Model model, HttpSession session) {
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        String op = request.getParameter("op");
        //获取需要修改的新闻id
        int id2 = Integer.parseInt(op);
        News news = userService.queryNewsByNewsId(id2);
        model.addAttribute("news",news);
        return "updateNews";
    }

    @RequestMapping("news_update")
    public String test03(@RequestParam String title,String link, String category, HttpServletRequest request, Model model, HttpSession session) {
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        String newsid = request.getParameter("newsid");
        int id = Integer.parseInt(newsid);
        userService.updateNewsByNewsId(title,link,category,id);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);

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
                        int id2 = vo.getNews().getId();
                        if (id2 == i) {
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
                        int id2 = vo.getNews().getId();
                        if (id2 == i) {
                            vo.setLike(-1);
                        }
                    }
                }
            }
        }
        model.addAttribute("vos",newsList);
        jedis.close();
        return "personal";
    }
}
