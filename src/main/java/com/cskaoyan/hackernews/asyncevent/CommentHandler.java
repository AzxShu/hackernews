package com.cskaoyan.hackernews.asyncevent;

import com.cskaoyan.hackernews.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//站内信息处理器，发送点赞和评论信息
@Component
public class CommentHandler implements EventHandler{

    @Autowired
    MessageService messageService;


    List<EventType> interestingEventType;
    //注册
    {
        interestingEventType = new ArrayList<EventType>();
        interestingEventType.add(EventType.COMMENT);
    }

    @Override
    public List<EventType> getInterestingEventType() {
        return interestingEventType;
    }

    @Override
    public void handleEvent(Event event) {
        String countbyAdmin ="系统提示！:" +
                "用户:"+event.getExtData().get("actionUserName")+
                " 评论了您的新闻分享:"+event.getExtData().get("AimNewTitile");
        String ownerUserName = (String) event.getExtData().get("ownerUserName");
        messageService.addMessage(1,
                ownerUserName,
                countbyAdmin);
    }
}
