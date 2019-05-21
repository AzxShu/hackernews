package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.asyncevent.EventProducer;
import com.cskaoyan.hackernews.asyncevent.EventType;
import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;
import com.cskaoyan.hackernews.converter.KeyWordFilter;
import com.cskaoyan.hackernews.service.NewsService;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class NewsController {

    @Autowired
    UserService userService;

    Jedis jedis = JedisUtils.getJedisFromPool();
    //图片上传
    @RequestMapping("uploadImage")
    @ResponseBody
    public Map uploadImage(HttpSession session, @RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffixName;
        URL resource =
                MainController.class.getClassLoader().getResource("static/images");
        String path = resource.getPath();
        File file1 = new File(path +"/"+ fileName);
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<Object, Object> hashMap = new HashMap<>();
        hashMap.put("code",0);
        hashMap.put("msg","http://localhost/images/"+fileName);
        session.setAttribute("filepath","http://localhost/images/"+fileName);
        return hashMap;
    }



    @RequestMapping("user/addNews")
    @ResponseBody
    public Map image(@RequestParam String title, String link , String fenlei,HttpSession session,HttpServletRequest request){
        String imagepath = (String) session.getAttribute("filepath");
        //获取新闻的图片的链接和标题，链接后将其插入到数据库中
        User user = (User) session.getAttribute("user");
        userService.insertNews(user.getId(),title,link,imagepath,new Date(),0,0,fenlei);
        Map<Object, Object> hashMap = new HashMap<>();
        session.removeAttribute("filepath");
        return hashMap;
    }


    @Autowired
    NewsService newsService;

    @RequestMapping("news/{id}")
    public String testcase01(@PathVariable("id") String id, HttpServletRequest request, HttpSession session, Model model){
        String contextPath = request.getContextPath();
        request.setAttribute("contextPath",contextPath);
        session.setAttribute("newsId",id);
        VoBean voBean = newsService.queryNewsById(id);
        User user = (User) session.getAttribute("user");
        if (user!=null){
            Boolean sismember = jedis.sismember("usersetlike"+id, ""+ user.getId());
            if (sismember) {
                System.out.println("sismember");
                model.addAttribute("like",1);
            }else {
                request.setAttribute("like",0);
            }
            Boolean sismember2 = jedis.sismember("usersetdislike"+id, ""+ user.getId());
            if (sismember2) {
                System.out.println("sismember2");
                request.setAttribute("like",-1);
            }else {
                request.setAttribute("like",0);
            }
        }else {
            request.setAttribute("like",0);
        }
        News news = voBean.getNews();
        User user2 = voBean.getUser();
        request.setAttribute("owner",user2);
        request.setAttribute("news",news);
        System.out.println(news);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String format = sdf.format(news.getCreatedDate());

        request.setAttribute("time",format);
        List<VoBeanOfCommentAndUser> list
                = newsService.queryNewsCommentByNewId(id);
        request.setAttribute("comments",list);
        System.out.println("list:"+list);
        return "detail";
    }

    //点赞
    @RequestMapping("like")
    @ResponseBody
    public Map testase02(HttpSession session,@RequestParam String newsId) {
        Map<Object, Object> map = new HashMap<>();
        //获取到点赞的新闻id
        System.out.println(newsId);
        //再获取点赞的用户id
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        if (user != null) {
            //将点赞的用户名放入set集合中，若set的长度发生变化则执行添加操作，若没有变化则不变
            //msg返回的是点赞后的赞数
            Set<String> smembers1 = jedis.smembers("usersetlike"+newsId);
            jedis.sadd("usersetlike"+newsId,""+id);
            //判断新加入的id是否已经存在
            if (jedis.sismember("usersetlike"+newsId,""+id)){
                //数据进行加一操作
                newsService.addAndQueryPointByNewsId(newsId);
                //查询该用户是否对该新闻进行点踩操作,如果有就进行删除
                if (jedis.sismember("usersetdislike"+newsId,""+id)){
                    jedis.srem("usersetdislike"+newsId,""+id);
                }
            }
            Set<String> smembers2 = jedis.smembers("usersetlike"+newsId);
            News news = newsService.queryPointByNewsId(newsId);
            int userId = news.getUserId();
            User user1 = userService.queryUserById(news.getUserId()+"");
            map.put("msg", ""+news.getLikeCount());
            map.put("code", 0);
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("actionUserName",user.getName());
            hashMap.put("ownerUserName", user1.getName());
            hashMap.put("aimNewTitile",news.getTitle());
            EventProducer.fireEvent(EventType.LIKE,id,user1.getId(),news.getUserId(),1,hashMap);
            jedis.close();
            return map;

        } else {
            map.put("code",1);
            map.put("msg", 0);
            return map;
        }
    }

    //点👎
    @RequestMapping("dislike")
    @ResponseBody
    public Map testase03(HttpSession session,@RequestParam String newsId) {
        Map<Object, Object> map = new HashMap<>();
        //获取到点踩的新闻id
        //再获取点踩的用户id
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        if (user != null) {
            Set<String> smembers1 = jedis.smembers("usersetdislike"+newsId);
            jedis.sadd("usersetdislike"+newsId,""+id);
            //判断新加入的id是否已经存在
            if (jedis.sismember("usersetlike"+newsId,""+id)){
                jedis.srem("usersetlike"+newsId,""+id);
                newsService.dislikePointByNewsId(newsId);
            }
            Set<String> smembers2 = jedis.smembers("usersetdislike"+newsId);
            News news = newsService.queryPointByNewsId(newsId);
            map.put("msg", ""+news.getLikeCount());
            map.put("code", 0);
            return map;
        } else {
            map.put("code",1);
            map.put("msg", 0);
            return map;
        }

    }

    @RequestMapping("addComment")
    public String addComment(HttpSession session,@RequestParam String content){
        String id = (String) session.getAttribute("newsId");
        User user = (User) session.getAttribute("user");
        int userid = user.getId();
        KeyWordFilter keyWordFilter = new KeyWordFilter();

        //对评论信息进行检索
        String s = keyWordFilter.testcase01(content);

        //拿到所有需要的条件之后，就可以进行插入啦
        newsService.insertNewComment(s,userid,id,1,new Date());
        //获取新闻发布者的id by newId
        News news = newsService.selectNewOwnerIdById(id);
        int i = Integer.parseInt(id);
        //插入comment之后还要更新一下news表中的coment数据
        newsService.updataCommentCount(id);
        User user1 = userService.queryUserById("" + news.getUserId());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("actionUserName",user.getName());
        hashMap.put("AimNewTitile",news.getTitle());
        hashMap.put("content",content);
        hashMap.put("ownerUserName",user1.getName());
        EventProducer.fireEvent(EventType.COMMENT,userid,news.getUserId(),i,1,hashMap);

        return "redirect:/news/"+id+"";
    }
}
