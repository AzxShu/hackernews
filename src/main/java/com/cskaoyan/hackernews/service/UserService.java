package com.cskaoyan.hackernews.service;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;

import java.util.Date;
import java.util.List;

public interface UserService {
    void insertNews(int id, String title, String link, String imagepath, Date date, int like_count, int comment_count);

    List<VoBean> queryAllNews();

    User queryUserById(String id);
}
