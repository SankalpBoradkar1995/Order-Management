package com.process.orders.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ObjectMappeprBean {

	@Bean
	public ObjectMapper objectMapper() {
		// Create the ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // Enable pretty printing

		return objectMapper;
	}

}
