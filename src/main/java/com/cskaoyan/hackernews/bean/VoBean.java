package com.cskaoyan.hackernews.bean;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoBean {
    User user;
    News news;
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
                '}';
    }
}
