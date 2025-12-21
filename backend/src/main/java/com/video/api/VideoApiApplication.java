package com.video.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //Temporary exclusion until proper repository is added
public class VideoApiApplication {

	static void main(String[] args) {
		SpringApplication.run(VideoApiApplication.class, args);
	}

}
