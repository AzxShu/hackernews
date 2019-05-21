package com.cskaoyan.hackernews.asyncevent;

import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.hackernews.util.JedisUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.*;

//取消息的流程:
    /*
    不同模块需要注册自己需要处理的事件，并提供相应的事件处理器
    取消息
    看具体的消息类型，不同的类型查询该类型的注册处理器
    然后再进行调用
    */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /*
    * 存放key value形式数据
    * key是指具体的事件类型
    * value是对该事件感兴趣的处理器*/
    private HashMap<EventType, List<EventHandler>> registerTable = new HashMap<>();

    /*
    * 项目初始化的时候去做,不同模块去注册自己需要处理的事件，并提供相应的事务处理器
    * 从spring容器中取*/
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> handlerMap
                = applicationContext.getBeansOfType(EventHandler.class);
        //拿到所有的handler之后，将他们填充进注册表中
        Set<Map.Entry<String, EventHandler>> entries = handlerMap.entrySet();
        //首先把map的每一个handler取出来
        for (Map.Entry e : entries
        ) {
            //取出map中的一个handler
            EventHandler handler = (EventHandler) e.getValue();
            //获取当前handler需要的事件
            List<EventType> interestingEventType = handler.getInterestingEventType();
            for (EventType type : interestingEventType
            ) {
                //如果注册表中已经有了这个key
                if (registerTable.containsKey(type)) {
                    List<EventHandler> eventHandlers = registerTable.get(type);
                    eventHandlers.add(handler);
                } else {
                    //没有key
                    ArrayList<EventHandler> eventHandlers =
                            new ArrayList<>();
                    eventHandlers.add(handler);
                    registerTable.put(type, eventHandlers);
                }
            }
        }
        Jedis jedis = JedisUtils.getJedisFromPool();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    //从消息队列里面取事件
                    //如果消息队列中没有事件，应该在此阻塞
                    //brpop返回一个含有两个元素的列表
                    List<String> msgQueue = jedis.brpop(60*60*24,"msgQueue");
                    String eventJson = msgQueue.get(1);
                    //把消息队列中的消息取出来变成Event对象
                    Event event = JSONObject.parseObject(eventJson, Event.class);
                    EventType eventtype = event.getEVENTTYPE();
                    //注册该事件的处理器
                    List<EventHandler> eventHandlers = registerTable.get(eventtype);
                    for (EventHandler h : eventHandlers
                    ) {
                        h.handleEvent(event);
                    }
                }
            }
        }.start();
     }


    //系统会通过该回调函数把容器的引用返回出来
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
