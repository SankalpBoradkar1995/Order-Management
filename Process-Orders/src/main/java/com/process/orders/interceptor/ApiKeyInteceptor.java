package com.process.orders.interceptor;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInteceptor implements HandlerInterceptor {

	@Value("${api.key}")
	private String API_KEY;

	@Autowired
	private Map<String, Boolean> allowedUrisMap;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		String requestApiKey = request.getHeader("API-KEY");
		System.out.println("requestApiKey is: " + requestApiKey);
		boolean isAllowed = allowedUrisMap.keySet().stream()
				.anyMatch(uri -> pathMatcher.match(uri, request.getRequestURI()));

		if (isAllowed) {
			return true;
		} else if (requestApiKey == null || !API_KEY.equals(requestApiKey)) {
			response.setStatus(response.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid API Key");
			return false;
		}

		return true;
	}

}
