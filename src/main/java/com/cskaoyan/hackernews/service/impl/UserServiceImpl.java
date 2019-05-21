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
    public void insertNews(int id, String title, String link, String imagepath, Date date, int like_count, int comment_count, String fenlei) {
        userDao.insertNews(id, title,link,imagepath,date,like_count,comment_count,fenlei);
    }

    @Override
    public List<VoBean> queryAllNews() {
        return userDao.queryAllNews();
    }

    @Override
    public User queryUserById(String id) {
        return userDao.queryUserById(id);
    }

    @Override
    public User queryUserByName(String toName) {
        return userDao.queryUserByName(toName);
    }

    @Override
    public List<VoBean> queryAllNewsOfUserById(int id) {
        return userDao.queryAllNewsOfUserById(id);
    }

    @Override
    public List<VoBean> queryAllNewsByPage(int i, int i1) {
        return userDao.queryAllNewsByPage(i, i1);
    }

    @Override
    public List<VoBean> queryAllNewsOfHotNews() {
        return userDao.queryAllNewsOfHotNews();
    }

    @Override
    public List<VoBean> queryAllNewsOfRecent() {
        return userDao.queryAllNewsOfRecent();
    }

    @Override
    public void updateNewsScore(int id, double score) {
        userDao.updateNewsScore(id,score);
    }

    @Override
    public List<VoBean> queryAllNewsByCategory(String category) {
        return userDao.queryAllNewsByCategory(category);
    }

    @Override
    public List<VoBean> queryAllNewsByCategoryByPage(int i, int i1, String category) {
        return userDao.queryAllNewsByCategoryByPage(i, i1,category);
    }

    @Override
    public void removeUserById(int id) {
        userDao.removeUserById(id);
    }

    @Override
    public News queryNewsByNewsId(int id2) {
        return userDao.queryNewsByNewsId(id2);
    }

    @Override
    public void updateNewsByNewsId(String title, String link, String category, int newsid) {
        userDao.updateNewsByNewsId(title,link,category,newsid);
    }
}
