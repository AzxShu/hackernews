package com.cskaoyan.hackernews.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


	/**
	 * 说明：对比两个时间格式的字符串大小
	 * 返回值：1:时间A>时间B / 2:时间A<时间B / 2:相等  /  -1异常
	 * @author Ashu
	 * 2018 8 16
	 */
	public static int compareDate(String d1,String d2,String geshi) {
		try{
			SimpleDateFormat sd1 = new SimpleDateFormat(geshi);
			SimpleDateFormat sd2 = new SimpleDateFormat(geshi);
			Date dt1 = sd1.parse(d1);
	        Date dt2 = sd2.parse(d2);
	        if (dt1.getTime() > dt2.getTime()) {
	            return 1;//时间1>时间2
	        } else if (dt1.getTime() < dt2.getTime()) {
	            return 2;//时间1<时间2
	        } else {//相等
	            return 2;
	        }
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 说明：将小时前和分钟前转为正确时间
	 * @author Ashu
	 * 2018 8 21
	 */
	public static String  toHtmlTime(String strDate){
		if(strDate.indexOf("年")!=-1||strDate.indexOf("日")!=-1){
			strDate = strDate.replace("年", "-").replace("月", "-").replace("日", "").replace("时", ":").replace("分", "");
		}
		//将其余网站的小时前 转换为大概时间
	   	 if(strDate.indexOf("小时")!=-1){
	   		 String time = strDate.replace("小时前","").trim();
	   		 long timeLong = Integer.valueOf(time).intValue()*3600000;
	   		 String timeStr = timeUtil("yyyy-MM-dd HH:mm:ss",false,timeLong);
	   		 if(timeStr!=null){
	   			 strDate=timeStr;
	   		 }
	   	 }else if(strDate.indexOf("分钟")!=-1){
	   		 String time = strDate.replace("分钟前","").trim();
	   		 long timeLong = Integer.valueOf(time).intValue()*60000;
	   		 String timeStr  = timeUtil("yyyy-MM-dd HH:mm:ss",false,timeLong);
	   		 if(timeStr!=null){
	   			 strDate=timeStr;
	   		 }
	   	 }else if(strDate.indexOf("今天")!=-1){
	   		 //将微博时间中的今天转为准确时间
	   		String time = strDate.replace("今天","").trim()+":00";
	   		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd ");
	   		strDate = sd.format(new Date())+time;
	   	 }else if(strDate.indexOf("秒前")!=-1){
	   		 //将微博中的秒前转为正确时间
	   		 String time = strDate.replace("秒前","").trim();
	   		 long timeLong = Integer.valueOf(time).intValue()*1000;
	   		 String timeStr  = timeUtil("yyyy-MM-dd HH:mm:ss",false,timeLong);
	   		 if(timeStr!=null){
	   			 strDate=timeStr;
	   		 }
	   	 }
	   	 return strDate;
	}
	
	
	/**
	 * 说明：当前时间 +- 毫秒  然后转为对应的时间格式
	 * 参数1：格式 (yyyy-MM-dd hh:mm:ss)       参数2：+-（true/false）   参数3：毫秒数
	 * 返回：日期格式字符串(异常返回NULL)
	 * @author Ashu
	 * 2018 8 16
	 */
	public static String timeUtil(String geshi,boolean jiajian,long haomiao){
		try{
			long timeLong = 0;
			if(!jiajian){
				 timeLong =new Date().getTime()-haomiao;
			}else{
				timeLong =new Date().getTime()+haomiao;
			}
			 SimpleDateFormat sd = new SimpleDateFormat(geshi);
			 return sd.format(new Date(timeLong));
		 }catch(Exception e){
			 e.printStackTrace();
			 System.out.println("时间转换异常！");
			 return null;
		 }
	}
}
