package com.cskaoyan.hackernews.asyncevent;

import java.util.List;

public interface EventHandler {
    //获取当前事件处理器感兴趣的事件类型
    List<EventType> getInterestingEventType();

    //当前事件处理器对事件的处理函数
    public void handleEvent(Event event);
}
