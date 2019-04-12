package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;
import com.cskaoyan.hackernews.service.NewsService;
import com.cskaoyan.hackernews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Controller
public class NewsController {

    @Autowired
    UserService userService;

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
    public Map image(@RequestParam String title, String link , HttpSession session){
        String imagepath = (String) session.getAttribute("filepath");
        //获取新闻的图片的链接和标题，链接后将其插入到数据库中
        User user = (User) session.getAttribute("user");
        System.out.println(new Date());
        userService.insertNews(user.getId(),title,link,imagepath,new Date(),0,0);
        Map<Object, Object> hashMap = new HashMap<>();
        session.removeAttribute("filepath");
        return hashMap;
    }

    @Autowired
    NewsService newsService;
    @RequestMapping("news/{id}")
    public String testcase01(@PathVariable("id") String id,HttpServletRequest request,HttpSession session){
        String contextPath = request.getContextPath();
        request.setAttribute("contextPath",contextPath);
        session.setAttribute("newsId",id);
        VoBean voBean = newsService.queryNewsById(id);
        News news = voBean.getNews();
        User user = voBean.getUser();
        request.setAttribute("owner",user);
        request.setAttribute("news",news);
        List<VoBeanOfCommentAndUser> list
                = newsService.queryNewsCommentByNewId(id);
        request.setAttribute("comments",list);
        return "detail";
    }

    @RequestMapping("like")
    @ResponseBody
    public Map testase02( HttpSession session){
        Object attribute = session.getAttribute("user");
        Map<Object, Object> map = new HashMap<>();
        if (attribute != null){
            map.put("msg","0");
            map.put("code",0);
            return map;
        }else {
            map.put("msg","你还没有登陆，请先登陆");
            map.put("code",1);
            return map;
        }
    }

    @RequestMapping("addComment")
    public String addComment(HttpSession session,@RequestParam String content){
        String id = (String) session.getAttribute("newsId");
        User user = (User) session.getAttribute("user");
        int userid = user.getId();
        //拿到所有需要的条件之后，就可以进行插入啦
        newsService.insertNewComment(content,userid,id,1,new Date());
        //插入comment之后还要更新一下news表中的coment数据
        newsService.updataCommentCount(id);
        return "redirect:/news/"+id+"";
    }
}
