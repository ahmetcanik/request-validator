package com.github.ahmetcanik.validator.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Component
public class CollectorRequestInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestBody = request.getReader().lines().collect(Collectors.joining());
		CollectorRequestJsonValidator.validate(requestBody);
		return true;
	}
}
