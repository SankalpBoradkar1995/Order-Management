package com.process.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProcessOrders {

	public static void main(String[] args) {
		 SpringApplication.run(ProcessOrders.class, args);

	}

}
