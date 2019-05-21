package com.cskaoyan.hackernews.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JsonJsoupUtil {

	/**
	 * 说明：根据规则解析HTML   1.0版本
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static void main(String[] args) {
		//getJson("json/test.json");
		try{
			//抓取百度
			String url = "http://news.baidu.com/ns?word=%B0%B2%BC%E0&bs=%B0%B2%BC%E0&sr=0&cl=2&rn=20&tn=news&ct=0&clk=sortbytime";
			List<Map<String,String>> listMap = getHTMLListMap(url,"json/baidu.json");
			for (Map<String, String> map : listMap) {
				System.out.println(map.toString());
			}
			//抓取新浪
			String urlsina = "http://search.sina.com.cn/?q=%E5%AE%89%E7%9B%91&c=news&from=channel&ie=utf-8";
			List<Map<String,String>> listMapsina = getHTMLListMap(urlsina,"json/sina.json");
			for (Map<String, String> map : listMapsina) {
				System.out.println(map.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 说明：json子节点中带有json 的解析方法
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static List<List> toJsonIter(JSONObject jsonContent){
		List<List> listAll = new ArrayList<List>();
		Iterator iter = jsonContent.keys();
		while(iter.hasNext()){  
			List list= new ArrayList();
			// 获得key  
			String key = (String) iter.next();  
			// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
			try {
				JSONObject valueJson = jsonContent.getJSONObject(key);
				list.add(key);
				list.add(valueJson);
				listAll.add(list);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}  
			 
		}  
		return listAll;
	}
	/**
	 * 说明：json子节点中不带有json 的解析方法
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static List<List> toJsonStringIter(JSONObject jsonContent){
		List<List> listAll = new ArrayList<List>();
		Iterator iter = jsonContent.keys();
		while(iter.hasNext()){  
			List list= new ArrayList();
			// 获得key  
			String key = (String) iter.next();  
			// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
			try {
				String valueJson = (String) jsonContent.get(key);
				list.add(key);
				list.add(valueJson);
				listAll.add(list);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}  
			 
		}  
		return listAll;
	}

	/**
	 * 
	* @Title: getHTMLListMap
	* @Description: 根据json解析html的main方法
	* @author Ashu
	* @date  2019年4月1日 下午8:10:20
	* @param urlJsoup
	* @param jsonName
	* @return
	 */
	public static List<Map<String,String>> getHTMLListMap(String urlJsoup,String jsonName){
		List<Map<String,String>> listMap  =new ArrayList<Map<String,String>>();
		try{
			Document document = null;
			//判断是url还是html标签
			if(urlJsoup.substring(0,4).indexOf("http")!=-1){
				System.out.println("urlJsoup:"+urlJsoup);
				document = Jsoup.connect(urlJsoup).get();
			}else{
				document = Jsoup.parse(urlJsoup);
			}
			String json = getJson(jsonName);
			JSONObject jsonYuan = new JSONObject(json);
			String htmlContent = (String) jsonYuan.get("HtmlContent"); //获得属性内容HtmlContent
			String strArr[] = getKey(htmlContent,"=");
			Object ojcet= getObject(document,strArr);
			if(null == ojcet){
				//为空直接返回
				return null;
			}else if(ojcet instanceof String){
				//如果返回的是String则代表直接return
				Map<String,String> map = new HashMap<String,String>();
				map.put("HtmlContent", (String)ojcet);
			}else if(ojcet instanceof Elements){
				Element oelem = ((Elements) ojcet).get(0);
				JSONObject itmJson = jsonYuan.getJSONObject("itm");//获得详细规则json
				String itmKey = (String) itmJson.get("key");//解析键
				String itmStrArr[] = getKey(itmKey,"=");
				Object itmOjcet= getObject(oelem,itmStrArr);
				if(itmOjcet instanceof Elements){
					JSONObject jsonContent =  itmJson.getJSONObject("content");//解析内容json
					List<List> listAll = toJsonIter(jsonContent);
					Elements els = (Elements) itmOjcet;//等待解析内容集合
					for(int i =0;i<els.size();i++){
						Map<String,String> map = new HashMap<String,String>();
						Element el = els.get(i);
						if(null != listAll&& listAll.size()>0){
							for (List li : listAll) {//只有下标0和1
								//li.get(0);//map 的 key
								JSONObject listValJson  = (JSONObject) li.get(1);
								String id = (String) listValJson.get("id");
								String itmIdArr[] = getKey(id,"=");
								Object itmIdOjcet= getObject(el,itmIdArr);
								Object elId =null;
								if(itmIdOjcet instanceof Elements){
									elId = ((Elements) itmIdOjcet).get(0);
								}else if(itmIdOjcet instanceof Element){
									elId = (Element) itmIdOjcet;
								}else {
									continue;
								}
								List<List> listValAll = toJsonStringIter(listValJson);
								if(null != listValAll&& listValAll.size()>0){
									for(List lival : listValAll){
										String keyA = (String) lival.get(0);
										if(!"id".equals(keyA)){
											String iterNoIdStrArr[] = getKey((String)lival.get(1),"=");
											elId = getObject(elId,iterNoIdStrArr);
										}
									}
									if(elId instanceof String){
										if(null !=elId &&!"".equals(((String) elId).trim())){
											map.put((String)li.get(0),(String)elId);
										}
									}
								}
							}
							listMap.add(map);
						}
					}
				}else if(itmOjcet instanceof Element){
					
				}
				return listMap;
			}else if(ojcet instanceof Element){
				//开始在此区域进行下一步解析
				//开始在此区域进行下一步解析
				JSONObject itmJson = jsonYuan.getJSONObject("itm");//获得详细规则json
				String itmKey = (String) itmJson.get("key");//解析键
				String itmStrArr[] = getKey(itmKey,"=");
				Object itmOjcet= getObject(ojcet,itmStrArr);
				if(itmOjcet instanceof Elements){
					JSONObject jsonContent =  itmJson.getJSONObject("content");//解析内容json
					List<List> listAll = toJsonIter(jsonContent);
					Elements els = (Elements) itmOjcet;//等待解析内容集合
					for(int i =0;i<els.size();i++){
						Map<String,String> map = new HashMap<String,String>();
						Element el = els.get(i);
						if(null != listAll&& listAll.size()>0){
							for (List li : listAll) {//只有下标0和1
								//li.get(0);//map 的 key
								JSONObject listValJson  = (JSONObject) li.get(1);
								String id = (String) listValJson.get("id");
								String itmIdArr[] = getKey(id,"=");
								Object itmIdOjcet= getObject(el,itmIdArr);
								Object elId =null;
								if(itmIdOjcet instanceof Elements){
									if(itmIdOjcet==null || ((Elements) itmIdOjcet).size()<=0){
										map=null;
										break;
									}
									elId = ((Elements) itmIdOjcet).get(0);
								}else if(itmIdOjcet instanceof Element){
									elId = (Element) itmIdOjcet;
								}else {
									map=null;
									break;
								}
								List<List> listValAll = toJsonStringIter(listValJson);
								if(null != listValAll&& listValAll.size()>0){
									for(List lival : listValAll){
										String keyA = (String) lival.get(0);
										if(!"id".equals(keyA)){
											String iterNoIdStrArr[] = getKey((String)lival.get(1),"=");
											elId = getObject(elId,iterNoIdStrArr);
										}
									}
									if(elId instanceof String){
										if(null !=elId &&!"".equals(((String) elId).trim())){
											map.put((String)li.get(0),(String)elId);
										}
									}
								}
							}
							if(map!=null&&map.size()>0){
								listMap.add(map);
							}
						}
					}
				}else if(itmOjcet instanceof Element){
					
				}
				return listMap;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 说明：根据节点的属性执行相应操作
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static Object  getObject(Object obj,String[] strArr){
		if(obj instanceof Document){
			return getKeyByDocument((Document)obj,strArr);
		}else if(obj instanceof Elements){
			return getKeyElements((Elements)obj,strArr);
		}else if(obj instanceof Element){
			return getKeyElement((Element)obj,strArr);
		}
		return null;
	}
	
	/**
	 * 说明：当类型为Document时调用
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static Object  getKeyByDocument(Document obj, String[] strArr){
		if(null == obj){
			return null;
		}
		if("id".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Element el = obj.getElementById(str);
							if(null != el && !"".equals(el.html().trim())){
								return el;
							}
						}
					}
				}
			}else{
				//获得对应ID的标签元素
				return obj.getElementById(strArr[1]);
			}
		}else if("class".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Elements els = obj.getElementsByClass(str);
							if(els.size()>0){
								return els;
							}
						}
					}
				}
			}else{
				//获得对应class的标签元素
				return obj.getElementsByClass(strArr[1]);
			}
		}else if("function".equals(strArr[0])){
			if(strArr[1].indexOf(":")!=-1){
				//代表fun调用的方法有参数 
				String[] funArr = getKey(strArr[1],":");//funArr[0] = 调用的方法名     funArr[1]=参数
				if(null != funArr && funArr.length>=2){
					if("select".equals(funArr[0])){
						return obj.select(funArr[1]);
					}else if("attr".equals(funArr[0])){
						return obj.attr(funArr[1]);
					}
				}
			}else{
				//代表调用的方法无参数
				if("ownText".equals(strArr[1])){
					return obj.ownText();
				}else if("text".equals(strArr[1])){
					return obj.text();
				}else if("outerHtml".equals(strArr[1])){
					return obj.outerHtml();
				
					
				}else if("html".equals(strArr[1])){
					return obj.html();
				}
			}
		}
		return null;
	}
	/**
	 * 说明：当类型为Elements时调用
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static Object  getKeyElements(Elements objs, String[] strArr){
		if(null == objs||objs.size()<=0){
			return null;
		}
		Element obj = objs.get(0);
		if("id".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Element el = obj.getElementById(str);
							if(null != el && !"".equals(el.html().trim())){
								return el;
							}
						}
					}
				}
			}else{
				//获得对应ID的标签元素
				return obj.getElementById(strArr[1]);
			}
		}else if("class".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Elements els = obj.getElementsByClass(str);
							if(els.size()>0){
								return els;
							}
						}
					}
				}
			}else{
				//获得对应class的标签元素
				return obj.getElementsByClass(strArr[1]);
			}
		}else if("function".equals(strArr[0])){
			if(strArr[1].indexOf(":")!=-1){
				//代表fun调用的方法有参数 
				String[] funArr = getKey(strArr[1],":");//funArr[0] = 调用的方法名     funArr[1]=参数
				if(null != funArr && funArr.length>=2){
					if("select".equals(funArr[0])){
						return obj.select(funArr[1]);
					}else if("attr".equals(funArr[0])){
						return obj.attr(funArr[1]);
					}
				}
			}else{
				//代表调用的方法无参数
				if("ownText".equals(strArr[1])){
					return obj.ownText();
				}else if("text".equals(strArr[1])){
					return obj.text();
				}else if("outerHtml".equals(strArr[1])){
					return obj.outerHtml();
				}else if("outerHtml".equals(strArr[1])){
					return obj.outerHtml();
				}else if("html".equals(strArr[1])){
					return obj.html();
				}
			}
		}
		return null;
	}
	/**
	 * 说明：当类型为Element时调用
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static Object  getKeyElement(Element obj, String[] strArr){
		if(null == obj){
			return null;
		}
		if("id".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Element el = obj.getElementById(str);
							if(null != el && !"".equals(el.html().trim())){
								return el;
							}
						}
					}
				}
			}else{
				//获得对应ID的标签元素
				return obj.getElementById(strArr[1]);
			}
		}else if("class".equals(strArr[0])){
			if(strArr[1].indexOf("|")!=-1){//存在 | 符号  代表为 多匹配
				String[] splitArr = strArr[1].split("\\|");
				if(splitArr.length>0){
					for (String str : splitArr) {
						if(!"".equals(str.trim())){
							Elements els = obj.getElementsByClass(str);
							if(els.size()>0){
								return els;
							}
						}
					}
				}
			}else{
				//获得对应class的标签元素
				return obj.getElementsByClass(strArr[1]);
			}
		}else if("function".equals(strArr[0])){
			if(strArr[1].indexOf(":")!=-1){
				//代表fun调用的方法有参数 
				String[] funArr = getKey(strArr[1],":");//funArr[0] = 调用的方法名     funArr[1]=参数
				if(null != funArr && funArr.length>=2){
					if("select".equals(funArr[0])){
						return obj.select(funArr[1]);
					}else if("attr".equals(funArr[0])){
						return obj.attr(funArr[1]);
					}
				}
			}else{
				//代表调用的方法无参数
				if("ownText".equals(strArr[1])){
					return obj.ownText();
				}else if("text".equals(strArr[1])){
					return obj.text();
				}else if("outerHtml".equals(strArr[1])){
					return obj.outerHtml();
				}else if("outerHtml".equals(strArr[1])){
					return obj.outerHtml();
				}else if("html".equals(strArr[1])){
					return obj.html();
				}
			}
		}
		return null;
	}
	public static String[] getKey(String keys,String check){
		String[] str={"",""};
		int index= keys.indexOf(check);
		if(index!=-1&&keys.length()>index){
			str[0] = keys.substring(0,index);
			str[1] = keys.substring(index+1);
		}else{
			return null;
		}
		return str;
	}

	/**
	 * 说明：获取规则信息
	 * @author 旋葎
	 * 2017 8 18
	 */
	public static String getJson(String jsonName){
		System.out.println("jsonName:"+jsonName);
		InputStream inputStream =JsonJsoupUtil.class.getClassLoader().getResourceAsStream(jsonName);
		byte[] byteArr = new byte[1024];
		System.out.println("in:"+inputStream);
		String str= "";
		try {
			while (inputStream.read(byteArr) != -1) {
				str += new String(byteArr,"utf-8");
			}
			inputStream.close();
			//System.out.println(str);
			inputStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		return str;
	}

}
