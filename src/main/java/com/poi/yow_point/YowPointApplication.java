package com.poi.yow_point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class YowPointApplication {

	public static void main(String[] args) {
		SpringApplication.run(YowPointApplication.class, args);
	}

}
