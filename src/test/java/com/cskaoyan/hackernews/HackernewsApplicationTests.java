package com.cskaoyan.hackernews;

import com.cskaoyan.hackernews.bean.News;
import com.cskaoyan.hackernews.bean.User;
import com.cskaoyan.hackernews.bean.VoBean;
import com.cskaoyan.hackernews.bean.VoBeanOfCommentAndUser;
import com.cskaoyan.hackernews.dao.NewsDao;
import com.cskaoyan.hackernews.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HackernewsApplicationTests {
	@Autowired
	NewsDao newsDao;

	@Test
	public void contextLoads() {
		List<VoBeanOfCommentAndUser> list = newsDao.queryNewsCommentByNewId(54);
		System.out.println(list);
	}

}
