package com.cskaoyan.hackernews;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.NewsPo;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.service.UserService;
import com.cskaoyan.hackernews.util.DateUtil;
import com.cskaoyan.hackernews.util.JedisUtils;
import com.cskaoyan.hackernews.util.NetNewsJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HackernewsApplicationTests {

	//jedis的测试类
	@Test
	public void testcase02() {
		//1,获取连接
		Jedis jedis = new Jedis("localhost", 6379);//使用空参构造,默认值就是localhost，6379
		//2,操作
		/*jedis.set("username","rty");*/
		String s = jedis.get("username");
		System.out.println(s);
		//3,关闭连接
		jedis.close();
	}

    @Test
    public void testcase03() {
        //1,获取连接
        Jedis jedis = new Jedis("localhost", 6379);//使用空参构造,默认值就是localhost，6379
        //2,操作
        jedis.hset("user1","name","qwe");
        jedis.hset("user1","age","123");
        jedis.hset("user1","gender","male");
        Map<String, String> map = jedis.hgetAll("user");
        System.out.println(map);
		Set<String> set = map.keySet();
		for (String key : set
			 ) {
			String s = map.get(key);
			System.out.println(s);
		}
		//3,关闭连接
        jedis.close();
    }

    //set的操作
	@Test
	public void testcase04() {
		//1,获取连接
		Jedis jedis = new Jedis();//使用空参构造,默认值就是localhost，6379
		//2,操作
		//set的存储
		jedis.sadd("myset03","java","php","c++");
		//set的获取
		Set<String> myset03 = jedis.smembers("myset03");
		System.out.println(myset03);
		//3,关闭连接
		jedis.close();
	}

	//sortset的操作
	@Test
	public void testcase05() {
		//1,获取连接
		Jedis jedis = new Jedis();//使用空参构造,默认值就是localhost，6379
		//2,操作
		jedis.zadd("mysortset01",3,"p1");
		jedis.zadd("mysortset01",4,"p2");
		jedis.zadd("mysortset01",7,"p3");
		jedis.zadd("mysortset01",1,"p4");
		//获取
		Set<String> mysortset01 = jedis.zrange("mysortset01", 0, -1);
		System.out.println(mysortset01);
		//3,关闭连接
		jedis.close();
	}

	//jedis连接池
	@Test
	public void testcase06() {
		//创建一个配置对象
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(50);
		config.setMaxIdle(10);

		//1,获取连接池
		JedisPool jedisPool = new JedisPool(config,"localhost",6379);
		//2,获取连接
		Jedis jedis = jedisPool.getResource();
		//3,使用
		String set = jedis.set("123", "456");

		//4,归还连接
		jedis.close();

	}
	@Test
	public void testcase07() {
		//创建一个配置对象
		/*JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(50);
		config.setMaxIdle(10);


		//1,获取连接池
		JedisPool jedisPool = new JedisPool(config,"localhost",6379);
		//2,获取连接
		Jedis jedis = jedisPool.getResource();*/
		/*JedisUtils.get()
		//3,使用
		String set = jedis.set("123", "456");*/

		//4,归还连接
		/*Jedis jedis = JedisUtils.getJedisFromPool();
		jedis.set("123", "777");
		jedis.close();*/
		/*Jedis jedis = JedisUtils.getJedisFromPool();
		Long userset01 = jedis.scard("userset");
		System.out.println(userset01);
		jedis.close();*/
	}
	@Test
	public void testcase08() {
		Jedis jedis = JedisUtils.getJedisFromPool();
		Boolean sismember = jedis.sismember("userset55", "1");
		System.out.println(sismember);
	}

	/*@Autowired
	ConversationService conversationService;

	@Test
	public void testcase09() {
		List<VoBeanOfUserAndMess> voBeanOfUserAndMesses = conversationService.queryListMess();
		System.out.println(voBeanOfUserAndMesses);
	}*/
	@Test
	public void testcase09() {
		List<NewsPo> sinaPutList = new NetNewsJson().getJsonPut(1, 10,"中国", "sina", DateUtil.toHtmlTime("100分钟前"));
		System.out.println(sinaPutList);
		List<NewsPo> baiduPutList = new NetNewsJson().getJsonPut(1, 10,	"123", "baidu", DateUtil.toHtmlTime("50分钟前"));
		System.out.println("---------"+baiduPutList);
	}

	@Autowired
	UserService userService;
	@Test
	public void testcase10(){
		Date date = new Date();
		long time = date.getTime();
		time = System.currentTimeMillis();
		/*long t1 = 1556346800161;
		Date date1 = new Date(t1);*/
		
		List<VoBean> list = userService.queryAllNewsOfUserById(9);
		for (VoBean v:list
			 ) {
			News news = v.getNews();
			Date createdDate = news.getCreatedDate();
			long time1 = createdDate.getTime();
			long test_time = time - time1;
			System.out.println("相差时间为:"+(test_time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)+"小时");
		}
	}

	@Test
	public void testcase11() {

		News news = userService.queryNewsByNewsId(5);
		System.out.println(news);
	}

}
