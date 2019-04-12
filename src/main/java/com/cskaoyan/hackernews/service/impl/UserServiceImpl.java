package com.cskaoyan.hackernews.service.impl;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public void insertNews(int id, String title, String link, String imagepath, Date date, int like_count, int comment_count) {
        userDao.insertNews(id, title,link,imagepath,date,like_count,comment_count);
    }

    @Override
    public List<VoBean> queryAllNews() {
        return userDao.queryAllNews();
    }

    @Override
    public User queryUserById(String id) {
        return userDao.queryUserById(id);
    }
}
