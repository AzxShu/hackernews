package com.cskaoyan.hackernews.dao;


import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface UserDao {
   void insertUser(@Param("email")String username, @Param("password")String password, @Param("headimg")String s);

    User queryUserIsExistByUsername(@Param("username") String username);

    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User queryUserIdByUsername(@Param("username") String username);

    void insertNews(@Param("id") int id, @Param("title") String title,
                    @Param("link") String link, @Param("imagepath") String imagepath,
                    @Param("date") Date date, @Param("like_count") int like_count,
                    @Param("comment_count") int comment_count,
                    @Param("fenlei") String fenlei);

    List<VoBean> queryAllNews();

    User queryUserById(@Param("id")String id);

    User queryUserByName(@Param("name") String toName);


    List<VoBean> queryAllNewsOfUserById(@Param("id") int id);

    List<VoBean> queryAllNewsByPage(@Param("page") int page, @Param("limit") int limit);

    List<VoBean> queryAllNewsOfHotNews();

 List<VoBean> queryAllNewsOfRecent();

 void updateNewsScore(@Param("newsid") int id, @Param("score") double score);

    List<VoBean> queryAllNewsByCategory(@Param("category") String category);

 List<VoBean> queryAllNewsByCategoryByPage(@Param("page") int page, @Param("limit") int limit, @Param("category") String category);

    void removeUserById(@Param("id") int id);

    News queryNewsByNewsId(@Param("id") int id2);

    void updateNewsByNewsId(@Param("title") String title,@Param("link") String link,@Param("category") String category,@Param("newsid") int newsid);
}
