package com.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class JwtTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtTestApplication.class, args);
	}
}
