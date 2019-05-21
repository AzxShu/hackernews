package com.cskaoyan.hackernews.service;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;

import java.util.Date;
import java.util.List;

public interface UserService {
    void insertNews(int id, String title, String link, String imagepath, Date date, int like_count, int comment_count, String fenlei);

    List<VoBean> queryAllNews();

    User queryUserById(String id);

    User queryUserByName(String toName);

    List<VoBean> queryAllNewsOfUserById(int id);

    List<VoBean> queryAllNewsByPage(int i, int i1);

    List<VoBean> queryAllNewsOfHotNews();

    List<VoBean> queryAllNewsOfRecent();

    void updateNewsScore(int id, double score);

    List<VoBean> queryAllNewsByCategory(String category);

    List<VoBean> queryAllNewsByCategoryByPage(int i, int i1, String category);

    void removeUserById(int id);

    News queryNewsByNewsId(int id2);

    void updateNewsByNewsId(String title, String link, String category, int newsid);
}
