package com.example.IceBreaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IceBreakingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IceBreakingApplication.class, args);
	}

}
