package com.cskaoyan.hackernews.bean;

public class VoBeanOfMessageAndUser {
    Message conversation;
    User user;

    public Message getConversation() {
        return conversation;
    }

    public void setConversation(Message conversation) {
        this.conversation = conversation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VoBeanOfMessageAndUser{" +
                "conversation=" + conversation +
                ", user=" + user +
                '}';
    }
}
