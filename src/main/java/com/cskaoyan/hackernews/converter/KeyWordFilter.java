package com.cskaoyan.hackernews.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【匹配度可以，速度较慢】
 * Java关键字过滤：http://blog.csdn.net/linfssay/article/details/7599262
 * @author ShengDecheng
 *
 */
public class KeyWordFilter {

	private static Pattern pattern = null;
	private static int keywordsCount = 0;

	// 从words.properties初始化正则表达式字符串
	private static void initPattern() {
		StringBuffer patternBuffer = new StringBuffer();
		try {
			//words.properties
			InputStream in = KeyWordFilter.class.getClassLoader().getResourceAsStream("keywords.properties");
			Properties property = new Properties();
			property.load(in);
			Enumeration<?> enu = property.propertyNames();
			patternBuffer.append("(");
			while (enu.hasMoreElements()) {
				String scontent = (String) enu.nextElement();
				patternBuffer.append(scontent + "|");
				//System.out.println(scontent);
				keywordsCount ++;
			}
			patternBuffer.deleteCharAt(patternBuffer.length() - 1);
			patternBuffer.append(")");
			pattern = Pattern.compile(patternBuffer.toString());
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	private static String doFilter(String str) {
		Matcher m = pattern.matcher(str);
//		while (m.find()) {// 查找符合pattern的字符串
//			System.out.println("The result is here :" + m.group());
//		}
		// 选择替换方式，这里以* 号代替
		str = m.replaceAll("*");
		return str;
	}

	/*public static void main(String[] args) {
		long startNumer = System.currentTimeMillis(); 
		initPattern();

		System.out.println("敏感词的数量：" + keywordsCount);
		String str = "傻逼bitch习近平毛泽东智障太多的伤yuming感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
	            + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"  
	            + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"  
	            + "然后法轮功 我们的扮演的角色就是跟随着主人yum公的喜红客联盟 怒哀20哀2015/4/16 20152015/4/16乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"  
	            + "关, 人, 流, 电, 发, 情, 太, 限, 法轮功, 个人, 经, 色, 许, 公, 动, 地, 方, 基, 在, 上, 红, 强, 自杀指南, 制, 卡, 三级片, 一, 夜, 多, 手机, 于, 自，"  
	            + "难过傻逼就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
		str = doFilter(str);
	}*/
	public String testcase01(String str){
		initPattern();
		str = doFilter(str);
		return str;
	}
}
