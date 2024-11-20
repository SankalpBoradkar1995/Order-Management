package com.process.orders.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllowedUriConfig {

	@Value("${allowed.uris}")
	private String allowedUris;
	
	@Bean
	public Map<String,Boolean> allowedUri()
	{
		List<String> uriList = List.of(allowedUris.split(","));
		return uriList.stream().collect(Collectors.toMap(uri ->uri, uri->true));
	}
}
