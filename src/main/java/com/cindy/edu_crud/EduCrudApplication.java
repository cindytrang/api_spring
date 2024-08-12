package com.cindy.edu_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.cindy.edu_crud")
public class EduCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduCrudApplication.class, args);
	}

}
