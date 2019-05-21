package com.cskaoyan.hackernews.asyncevent;

public enum EventType {
    LIKE(0),
    DISLIKE(1),
    COMMENT(2)
    ;

    private  int type;

    EventType(int type) {
        this.type = type;
    }
}
