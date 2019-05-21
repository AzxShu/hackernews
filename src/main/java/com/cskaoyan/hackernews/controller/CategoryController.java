package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.PageBean;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.JedisUtils;
import org.apache.ibatis.annotations.Param;
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
public class CategoryController {
    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;
    List<VoBean> list = new ArrayList<>();

    @RequestMapping("category/finance")
    public String testcase06(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("财经");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"财经");
        

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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/entertainment")
    public String testcase07(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("娱乐");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"娱乐");
        

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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/gaming")
    public String testcase08(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("电竞");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"电竞");
        

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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/political")
    public String testcase09(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("时政");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"时政");
        

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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/technology")
    public String testcase10(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("科技");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"科技");
        

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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/sport")
    public String testcase11(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("体育");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"体育");


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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/car")
    public String testcase12(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("汽车");

        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"汽车");


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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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

    @RequestMapping("category/history")
    public String testcase13(Model model, HttpServletRequest request, HttpSession session,@Param("page")String page){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        if (page == null) {
            page = "1";
        }
        int i = Integer.parseInt(page);
        List<VoBean> newsList = userService.queryAllNewsByCategory("历史");

        System.out.println("----------------"+newsList);
        List<VoBean> newsListbypage = userService.queryAllNewsByCategoryByPage((i-1)*10,10,"历史");


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
        for (VoBean vo:newsListbypage
        ) {
            Date createdDate = vo.getNews().getCreatedDate();
            long time = createdDate.getTime();
            Date date = new Date();
            long time1 = date.getTime();
            long test = time1-time;
            long l = (time1 - time) / (1000 * 3600);
            if (l <= 0){
                vo.getNews().setTime("刚刚");
            }else if(l>0&&l<=24){
                vo.getNews().setTime(l+"个小时前");
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
}
