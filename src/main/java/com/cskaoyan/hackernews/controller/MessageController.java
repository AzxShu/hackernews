package com.cskaoyan.hackernews.controller;

import com.cskaoyan.hackernews.bean.Msg;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBeanOfMessageAndUser;
import com.cskaoyan.hackernews.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @RequestMapping("/user/tosendmsg")
    public  String toSendMsg(HttpServletRequest request, Model model){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        return "sendmsg";
    }

    @RequestMapping("/user/msg/addMessage")
    public  String addMessage(HttpServletRequest request,String toName,String content){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        int id = user.getId();
        messageService.addMessage(id,toName,content);
        return "redirect:/msg/list";
    }

    @RequestMapping("/msg/list")
    public  String msgList(HttpServletRequest request, Model model){
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath",contextPath);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        List<VoBeanOfMessageAndUser> messageVoList= messageService.findUserMessageByUserId(user.getId());
        model.addAttribute("conversations" ,messageVoList);
        System.out.println("messageVoList:"+messageVoList);
        return "letter";
    }

    @RequestMapping("/msg/detail")
    public String letterDetail(HttpServletRequest request, Model model ,String conversationId){
        String contextPath = request.getContextPath();
        List<Msg> messageList= messageService.findMessageByconversationId(conversationId);
        model.addAttribute("contextPath",contextPath);
        model.addAttribute("messages",messageList);
        System.out.println("messageList:"+messageList);
        return "letterDetail";
    }
}
