package com.zei.happy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.zei.happy.mapper")
@EnableScheduling
public class ZhifubaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZhifubaoApplication.class, args);
	}

}
