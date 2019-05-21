package com.cskaoyan.hackernews.dao;

import com.cskaoyan.hackernews.bean.Msg;
import com.cskaoyan.hackernews.bean.VoBeanOfMessageAndUser;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface MessageDao {

    void addMessage(int from_id, int to_id, String content, Date date, int hasread, String s);

    List<VoBeanOfMessageAndUser> findUserMessageByUserId(@Param("id") int id);

   /* List<Msg> findUserMessageByconversationId(@Param("conversationId")String conversationId,
                                              @Param("conversationId2")String conversationId2);*/

    List<Msg> findMessageByconversationId(@Param("conversationId") String conversationId);
}
