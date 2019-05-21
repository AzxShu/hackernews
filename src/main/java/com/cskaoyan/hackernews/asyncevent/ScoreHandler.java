package com.cskaoyan.hackernews.asyncevent;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScoreHandler implements EventHandler{

    List<EventType> interestingEventType;
    {
        interestingEventType = new ArrayList<EventType>();
        interestingEventType.add(EventType.LIKE);
    }

    @Override
    public List<EventType> getInterestingEventType() {
        return interestingEventType;
    }

    @Override
    public void handleEvent(Event event) {
        System.out.println("点赞的用户:"+event.getActorID());
        System.out.println("被点赞的用户:"+event.getOwnerID());
    }
}
