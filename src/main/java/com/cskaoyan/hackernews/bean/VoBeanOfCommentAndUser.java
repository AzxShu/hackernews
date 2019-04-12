package com.cskaoyan.hackernews.bean;

public class VoBeanOfCommentAndUser {
    Comment comment;
    User user;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VoBeanOfCommentAndUser{" +
                "comment=" + comment +
                ", user=" + user +
                '}';
    }
}
