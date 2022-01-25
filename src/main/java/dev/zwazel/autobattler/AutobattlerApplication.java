package dev.zwazel.autobattler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class AutobattlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutobattlerApplication.class, args);
	}
}
