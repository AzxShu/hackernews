package com.cskaoyan.hackernews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
/*import org.springframework.boot.context.web.SpringBootServletInitializer;*/
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HackernewsApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HackernewsApplication.class);
	}

	 public static void main(String[] args) {
		SpringApplication.run(HackernewsApplication.class, args);
	}
}
