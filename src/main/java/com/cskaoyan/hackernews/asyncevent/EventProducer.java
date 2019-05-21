package com.cskaoyan.hackernews.asyncevent;

import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.hackernews.util.JedisUtils;
import redis.clients.jedis.Jedis;
import java.util.HashMap;

public class EventProducer {

    //产生一个事件，并放入到队列
    public static void fireEvent(EventType eventType, int actorId, int ownerId, int itemId, int itemType, HashMap extData){

        //产生事件
        Event event = new Event();
        event.setActorID(actorId);
        event.setOwnerID(ownerId);
        event.setEVENTTYPE(eventType);
        event.setItemID(itemId);
        event.setItemTyep(itemType);
        event.setExtData(extData);

        // 放到队列

        Jedis jedis  = JedisUtils.getJedisFromPool();

        //JSon

        //把Event 对象转为Json字符串
        String jsonString = JSONObject.toJSONString(event);
        jedis.lpush("msgQueue", jsonString );

    }
}
