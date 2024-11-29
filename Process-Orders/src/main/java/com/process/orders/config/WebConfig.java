package com.process.orders.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.process.orders.interceptor.ApiKeyInteceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	ApiKeyInteceptor apiKeyInterceptor;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the ApiKeyInterceptor for all URIs except login
        registry.addInterceptor(apiKeyInterceptor);
    }
}
