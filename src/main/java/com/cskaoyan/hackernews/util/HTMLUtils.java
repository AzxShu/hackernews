package com.cskaoyan.hackernews.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * HTML操作工具类。
 * @author Belen
 * @since 2013/05/25
 */
public class HTMLUtils {

	/**
	 * 通过BufferedInputStream,根据URL获取页面的源代码（源代码转化成小写字母返回）.
	 * 
	 * @param url
	 *            url地址
	 * @param encode
	 *            编码 如果encode为空,则以GBK形式转换. 
	 * @return 页面源代码
	 * @author Belen
	 * @throws HanyaException 
	 */
	public static String getHtml2(String url, String encode)
			throws Exception {
		if(url == null)
			throw new Exception("url",new NullPointerException("url")); 
		if(encode == null){
			encode = "GBK";
		}
		StringBuffer html = new StringBuffer();
		String result = null;
		URLConnection conn = null;
		BufferedInputStream in = null;
		try {
			URL u = new URL(url);
			conn = u.openConnection();
			conn.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
			// 设置连接主机超时（单位：毫秒）
			conn.setConnectTimeout(30000);
			// 设置从主机读取数据超时（单位：毫秒） 
			conn.setReadTimeout(30000); 
			in = new BufferedInputStream(conn.getInputStream());
			String inputLine;
			byte[] buf = new byte[4096];
			int bytesRead = 0;
			while (bytesRead >= 0) {
				inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
				html.append(inputLine);
				bytesRead = in.read(buf);
				inputLine = null;
			}
			buf = null;
			result = new String(html.toString().trim().getBytes("ISO-8859-1"),
					encode).toLowerCase();
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (conn != null) {
					conn = null;
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
		return result;
	}
	
	/**
	 * 通过ByteArrayOutputStream,根据URL获取页面的源代码（HTML源代码返回）.
	 * 
	 * @param url
	 * @param encode 以encode编码类型转换数据,如果encode为空,则以GBK形式转换. 
	 * @return
	 * @throws HanyaException 
	 */
	public static String getHtml(String url,String encode) throws Exception{
		if(url == null)
			throw new Exception("url",new NullPointerException("url"));
		if(encode == null){
			encode = "GBK";
		}
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			URL u = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
			// 设置user agent确保系统与浏览器版本兼容
			HttpURLConnection.setFollowRedirects(true);
		    httpConn.setRequestMethod("GET"); 
		    httpConn.setRequestProperty("User-Agent","Mozilla/4.0 " +
		    		"(compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
			in = u.openStream();
			// 设置连接主机超时（单位：毫秒）
			httpConn.setConnectTimeout(30000);
			// 设置从主机读取数据超时（单位：毫秒） 
			httpConn.setReadTimeout(30000); 
			int length = 0;
			bos = new ByteArrayOutputStream(); 
	        while((length = in.read()) != -1) { 
	        	 bos.write(length);
	        	 bos.flush();
	        }
	        in.close();
			return new String(bos.toByteArray(),encode);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception("流处理失败。"+e);
		} finally{
			try {
				if(in != null){
					in.close();
				}
				if(bos != null){
					bos.close();
				}
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}
	
	
	
	/**
	 * 说明：字符转码
	 * @author 旋葎
	 * 2017 8 15
	 */
	public static String convert(String utfString){  
	   StringBuilder sb = new StringBuilder();  
	   int i = -1;  
	   int pos = 0;  
	     
	   while((i=utfString.indexOf("\\u", pos)) != -1){  
	       sb.append(utfString.substring(pos, i));  
	       if(i+5 < utfString.length()){  
	           pos = i+6;  
	           sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));  
	       }  
	   }   
	   return sb.toString();  
	} 
}
