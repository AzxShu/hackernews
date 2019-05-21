package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class HotMessageController {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;
    List<VoBean> list = new ArrayList<>();

    @RequestMapping("/hot/hotshare")
    public String testcase06(Model model, HttpServletRequest request, HttpSession session){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        User user = (User) session.getAttribute("user");
        List<VoBean> newsList = userService.queryAllNewsOfHotNews();
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
        //分数计算，当有评论或者点赞数时
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
        List<VoBean> list = userService.queryAllNewsOfHotNews();
        model.addAttribute("vos",newsList);
        jedis.close();
        return "hotNews";
    }
}
