package com.cskaoyan.hackernews.bean;

import org.springframework.stereotype.Component;

import java.util.List;

public class VoBean {
    User user;
    News news;
    int like;
    public int getLike() {
        return like;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VoBean{" +
                "user=" + user +
                ", news=" + news +
                ", like=" + like +
                '}';
    }
}
