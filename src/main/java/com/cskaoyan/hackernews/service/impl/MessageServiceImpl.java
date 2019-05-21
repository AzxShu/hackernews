package com.cskaoyan.hackernews.service.impl;

import com.cskaoyan.hackernews.bean.Msg;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBeanOfMessageAndUser;
import com.cskaoyan.hackernews.dao.MessageDao;
import com.cskaoyan.hackernews.dao.UserDao;
import com.cskaoyan.hackernews.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    UserDao userDao;

    @Override
    public void addMessage(int id, String toName, String content) {
        Date date = new Date();
        User toUser= userDao.queryUserByName(toName);
        if(toUser!=null) {
            int toId = toUser.getId();
            System.out.println("toid:"+toId);
            System.out.println("fromid:"+id);
            if (id < toId) {
                messageDao.addMessage(id, toId, content, date,0,id+"_"+toId);
            }else{
                messageDao.addMessage(id, toId, content, date,0,toId+"_"+id);
            }
        }
    }
    @Override
    public List<VoBeanOfMessageAndUser> findUserMessageByUserId(int id) {
        return messageDao.findUserMessageByUserId(id);
    }

    @Override
    public List<Msg> findMessageByconversationId(String conversationId) {
        return messageDao.findMessageByconversationId(conversationId);
    }
}
