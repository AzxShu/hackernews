package com.cskaoyan.hackernews.service;

import com.cskaoyan.hackernews.bean.Msg;
import com.cskaoyan.hackernews.bean.VoBeanOfMessageAndUser;

import java.util.List;

public interface MessageService {
    void addMessage(int id, String toName, String content);

    List<VoBeanOfMessageAndUser> findUserMessageByUserId(int id);

    List<Msg> findMessageByconversationId(String conversationId);
}
