package com.process.orders;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.rabbit.config","com.process.orders"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
public class ProcessOrders {

	public static void main(String[] args) {
		 SpringApplication.run(ProcessOrders.class, args);

	}

}
