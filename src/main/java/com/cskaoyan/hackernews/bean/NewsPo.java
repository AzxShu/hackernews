package com.cskaoyan.hackernews.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/*import org.apache.commons.lang.StringUtils;
 import org.apache.hadoop.hbase.client.Put;
 import org.apache.log4j.chainsaw.Main;*/

/**
 * 
 * @ClassName: NewsPo
 * @Description: 新闻表-实体类 (这里用一句话描述这个类的作用)
 * @author Ashu
 * @date 2019年4月1日 下午6:21:30
 */
public class NewsPo {
	private int id;
	private String title; // 标题
	private String url; // url
	private String intro; // 内容
	private String current_time; // 系统时间
	private String datetime; // 新闻时间
	private String media; // 来源 sina / baidu / weibo
	private String plus_minus; // 正负面标识 0正面 / 1负面

	
	private String status;
	private int isDel;

	public NewsPo() {
		title = "";
		url = "";
		intro = "";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		current_time = sd.format(new Date());
		datetime = "";
		media = "";
	}

	public NewsPo(Map<String, String> map) {
		title = map.get("title");
		url = map.get("url");
		intro = map.get("intro");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		current_time = sd.format(new Date());
		datetime = map.get("datetime");
		media = map.get("media");
		// 正负面标识 0正面 / 1负面
		/*if (plus_minus == null) {
			try {
				if (UtilRegex.mark(title) == true) {
					plus_minus = "1";// 负面新闻
				} else {
					plus_minus = "0";// 正面新闻
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	public synchronized String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;
	}

	/**
	 * 说明：将当前PO对象转Put对象
	 * 
	 * @author wangxl 2017 8 17
	 */
	/*
	 * public Put toPut(){ String uuid = "0"; if("sina".equals(media)){ uuid =
	 * this.title+"sina"; }else if("baidu".equals(media)){ uuid =
	 * this.title+"baidu"; }else if("weibo".equals(media)){ uuid =
	 * this.title+"baidu"; } Put put = new Put((uuid).getBytes());
	 * put.addColumn("info".getBytes(), "title".getBytes(),
	 * System.currentTimeMillis(),this.title.getBytes());
	 * put.addColumn("info".getBytes(), "media".getBytes(),
	 * System.currentTimeMillis(),this.media.getBytes());
	 * put.addColumn("info".getBytes(), "datetime".getBytes(),
	 * System.currentTimeMillis(),this.datetime.getBytes());
	 * put.addColumn("info".getBytes(), "intro".getBytes(),
	 * System.currentTimeMillis(),this.intro.getBytes());
	 * put.addColumn("info".getBytes(), "url".getBytes(),
	 * System.currentTimeMillis(),this.url.getBytes());
	 * put.addColumn("info".getBytes(), "current_time".getBytes(),
	 * System.currentTimeMillis(),this.current_time.getBytes());
	 * put.addColumn("info".getBytes(), "plus_minus".getBytes(),
	 * System.currentTimeMillis(),this.plus_minus.getBytes()); return put; }
	 */

	/**
	 * 说明：校验对象中的所有变量是否均不为空(空格也算是空) 为空返回true
	 * 
	 * @author wangxl 2018 8 18
	 */
	/*
	 * public boolean isNotNull(){
	 * if(StringUtils.isBlank(title)||StringUtils.isBlank
	 * (url)||StringUtils.isBlank(current_time)
	 * ||StringUtils.isBlank(intro)||StringUtils
	 * .isBlank(media)||StringUtils.isBlank
	 * (datetime)||StringUtils.isBlank(plus_minus)){ return true; } return
	 * false; }
	 */

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
		// 正负面标识 0正面 / 1负面
		/*try {
			if (UtilRegex.mark(title) == true) {
				plus_minus = "1";// 负面新闻
			} else {
				plus_minus = "0";// 正面新闻
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getPlus_minus() {
		return plus_minus;
	}

	public void setPlus_minus(String plus_minus) {
		this.plus_minus = plus_minus;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	} 
	
	public String getTitle() {
		return title;
	}

	/*@Override
	public String toString() {
		return "NewsPo [title=" + title + ", url=" + url + ", intro=" + intro
				+ ", current_time=" + current_time + ", datetime=" + datetime
				+ ", media=" + media + ", plus_minus=" + plus_minus + "]";
	}*/

	@Override
	public String toString() {
		return "NewsPo{" +
				"id=" + id +
				", title='" + title + '\'' +
				", url='" + url + '\'' +
				", intro='" + intro + '\'' +
				", current_time='" + current_time + '\'' +
				", datetime='" + datetime + '\'' +
				", media='" + media + '\'' +
				", plus_minus='" + plus_minus + '\'' +
				", status='" + status + '\'' +
				", isDel=" + isDel +
				'}';
	}
}
