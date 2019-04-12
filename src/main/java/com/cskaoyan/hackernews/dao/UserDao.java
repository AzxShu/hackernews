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
    int insertUser(@Param("email") String email, @Param("password") String password);

    User queryUserIsExistByUsername(@Param("username") String username);

    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User queryUserIdByUsername(@Param("username") String username);

    void insertNews(@Param("id") int id, @Param("title") String title,
                    @Param("link") String link, @Param("imagepath") String imagepath,
                    @Param("date") Date date, @Param("like_count") int like_count,
                    @Param("comment_count") int comment_count);

    List<VoBean> queryAllNews();

    User queryUserById(@Param("id")String id);
}
