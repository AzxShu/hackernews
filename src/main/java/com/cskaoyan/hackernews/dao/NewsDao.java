package com.cskaoyan.hackernews.dao;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface NewsDao {
    VoBean queryNewsById(@Param("id") int id);

    void insertNewComment(@Param("content") String content,
                          @Param("userid")int userid,
                          @Param("newid")String newid,
                          @Param("i")int i,
                          @Param("date")Date date);

    List<VoBeanOfCommentAndUser> queryNewsCommentByNewId(@Param("id")int i);

    void updataCommentCount(@Param("id") int i);

    void addAndQueryPointByNewsId(@Param("newid") int i);

    News queryPointByNewsId(@Param("newid")int i);

    void dislikePointByNewsId(@Param("newid") int i);

    News selectNewOwnerIdById(@Param("newid")int id);
}
