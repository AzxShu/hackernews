package com.cskaoyan.hackernews.interceptor;

import com.cskaoyan.hackernews.bean.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

   @Autowired
   private HostHolder hostHolder;

   @Override
   public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    /*   if (hostHolder.getUser() == null) {
           httpServletResponse.sendRedirect("/?pop=0");//参数pop=1表示让登录框弹出
           return false;
       }
       return true;*/
    return true;
   }

   @Override
   public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
   }

   @Override
   public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
   }
}
