package com.cskaoyan.hackernews.service.impl;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;
import com.cskaoyan.hackernews.dao.NewsDao;
import com.cskaoyan.hackernews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsDao newsDao;

    @Override
    public VoBean queryNewsById(String id) {
        int i = Integer.parseInt(id);
        return newsDao.queryNewsById(i);
    }

    @Override
    public void insertNewComment(String content, int userid, String newid, int i, Date date) {
        newsDao.insertNewComment(content,userid, newid, i,date);
    }

    @Override
    public List<VoBeanOfCommentAndUser> queryNewsCommentByNewId(String id) {
        int i = Integer.parseInt(id);
        return newsDao.queryNewsCommentByNewId(i);
    }

    @Override
    public void updataCommentCount(String id) {
        int i = Integer.parseInt(id);
        newsDao.updataCommentCount(i);
    }
}
