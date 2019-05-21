package com.cskaoyan.hackernews.bean;

public class Msg {
      int userId;
      String headUrl;
      User user;
     Message message;

    public Msg() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "userId=" + userId +
                ", headUrl='" + headUrl + '\'' +
                ", user=" + user +
                ", message=" + message +
                '}';
    }
}
