package com.cskaoyan.hackernews.service;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;

import java.util.Date;
import java.util.List;

public interface NewsService {
    VoBean queryNewsById(String id);

    void insertNewComment(String content, int userid, String id, int i, Date date);

    List<VoBeanOfCommentAndUser> queryNewsCommentByNewId(String id);

    void updataCommentCount(String id);
}
