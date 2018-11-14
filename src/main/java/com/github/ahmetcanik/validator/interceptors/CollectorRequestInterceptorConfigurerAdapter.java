package com.github.ahmetcanik.validator.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class CollectorRequestInterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Autowired
	CollectorRequestInterceptor collectorRequestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(collectorRequestInterceptor);
	}
}
