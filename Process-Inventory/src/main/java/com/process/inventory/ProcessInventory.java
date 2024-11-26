package com.process.inventory;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.rabbit.config","com.process.inventory"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
public class ProcessInventory {

	public static void main(String[] args) {
		SpringApplication.run(ProcessInventory.class, args);

	}

}
