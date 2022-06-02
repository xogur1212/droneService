package com.xy124.drone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan("com.xy124")
@EntityScan("com.xy124")
@EnableJpaRepositories("com.xy124")
public class DroneApplication  {
	public static void main(String[] args) {
		SpringApplication.run(DroneApplication.class, args);
	}


}
