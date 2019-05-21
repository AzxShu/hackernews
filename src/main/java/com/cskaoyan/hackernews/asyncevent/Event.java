package com.cskaoyan.hackernews.asyncevent;


import org.springframework.stereotype.Component;

import java.util.HashMap;

//事件模型
//别人需要什么东西

public class Event {

   //事件的类型
     EventType EVENTTYPE; //0 : 点赞  1 点踩，2 回复  等

    //事件是谁触发  谁点赞
    int ActorID;

    //事件的作用目标  点给谁的
    int ownerID;

    // 下面两个是什么东西上的点赞

    int itemID  ; //哪个条目点赞

    int itemTyep ;  //那个具体的类型 新闻  评论

    //为了以后扩展
    HashMap<String,Object> extData;

    @Override
    public String toString() {
        return "Event{" +
                "EVENTTYPE=" + EVENTTYPE +
                ", ActorID=" + ActorID +
                ", ownerID=" + ownerID +
                ", itemID=" + itemID +
                ", itemTyep=" + itemTyep +
                ", extData=" + extData +
                '}';
    }

    public EventType getEVENTTYPE() {
        return EVENTTYPE;
    }

    public void setEVENTTYPE(EventType EVENTTYPE) {
        this.EVENTTYPE = EVENTTYPE;
    }

    public int getActorID() {
        return ActorID;
    }

    public void setActorID(int actorID) {
        ActorID = actorID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemTyep() {
        return itemTyep;
    }

    public void setItemTyep(int itemTyep) {
        this.itemTyep = itemTyep;
    }

    public HashMap<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(HashMap<String, Object> extData) {
        this.extData = extData;
    }
}
