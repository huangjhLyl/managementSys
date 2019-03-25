package com.docker.adminrole;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.docker.adminRole.modules.*.mapper")
public class AdminRoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminRoleApplication.class, args);
	}

}
