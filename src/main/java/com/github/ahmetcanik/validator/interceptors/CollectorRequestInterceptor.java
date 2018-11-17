package com.github.ahmetcanik.validator.interceptors;

import com.github.ahmetcanik.validator.data.repository.UaBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.UserAgentBlacklistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Component
public class CollectorRequestInterceptor implements HandlerInterceptor {
	private UaBlacklistRepository uaBlacklistRepository;

	@Autowired
	public CollectorRequestInterceptor(UaBlacklistRepository uaBlacklistRepository) {
		this.uaBlacklistRepository = uaBlacklistRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// check for user-agent whether it is blacklisted
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null && !userAgent.isEmpty()) {
			try {
				CollectorRequestValidator.validateUserAgent(userAgent, uaBlacklistRepository);
			} catch (UserAgentBlacklistedException e) {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());
				// stop the chain
				return false;
			}
		}

		String requestBody = request.getReader().lines().collect(Collectors.joining());
		CollectorRequestValidator.validateJson(requestBody);
		return true;
	}
}
