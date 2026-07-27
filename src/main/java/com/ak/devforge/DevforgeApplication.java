package com.ak.devforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DevforgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevforgeApplication.class, args);
	}

}
