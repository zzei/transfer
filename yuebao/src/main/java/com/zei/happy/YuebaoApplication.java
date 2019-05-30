package com.zei.happy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zei.happy.mapper")
public class YuebaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuebaoApplication.class, args);
	}

}
