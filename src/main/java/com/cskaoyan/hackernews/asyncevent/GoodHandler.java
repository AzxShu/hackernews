package com.cskaoyan.hackernews.asyncevent;

import com.cskaoyan.hackernews.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


//点赞操作
@Component
public class GoodHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    List<EventType> interestingEventType;

    {
        interestingEventType = new ArrayList<>();
        interestingEventType.add(EventType.LIKE);
    }

    @Override
    public List<EventType> getInterestingEventType() {
        return interestingEventType;
    }

    @Override
    public void handleEvent(Event event) {
        String countbyAdmin =
                "系统提示! "+
                        "用户:"+event.getExtData().get("actionUserName")+
                        "对您的新闻:"+event.getExtData().get("aimNewTitile")+",进行了点赞操作!";
        messageService.addMessage(1,
                event.getExtData().get("ownerUserName")+"",
                countbyAdmin);
    }
}
