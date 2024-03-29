package com.hussey.levelupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class LevelUpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LevelUpServiceApplication.class, args);
	}

}
