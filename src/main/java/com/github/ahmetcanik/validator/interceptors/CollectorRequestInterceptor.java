package com.github.ahmetcanik.validator.interceptors;

import com.github.ahmetcanik.validator.data.repository.HourlyStatsRepository;
import com.github.ahmetcanik.validator.data.repository.UaBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.exceptions.UserAgentBlacklistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CollectorRequestInterceptor implements HandlerInterceptor {
	private final UaBlacklistRepository uaBlacklistRepository;
	private final HourlyStatsRepository hourlyStatsRepository;
	private String requestBody;

	@Autowired
	public CollectorRequestInterceptor(UaBlacklistRepository uaBlacklistRepository, HourlyStatsRepository hourlyStatsRepository) {
		this.uaBlacklistRepository = uaBlacklistRepository;
		this.hourlyStatsRepository = hourlyStatsRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MyRequestWrapper myRequestWrapper = new MyRequestWrapper(request);
		requestBody = myRequestWrapper.getBody();

		// first log request of the customer
		// check for user-agent whether it is blacklisted
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null && !userAgent.isEmpty()) {
			try {
				CollectorRequestPreprocessor.validateUserAgent(userAgent, uaBlacklistRepository);
			} catch (UserAgentBlacklistedException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(e.getMessage());
				CollectorRequestPreprocessor.logRequest(requestBody, true, hourlyStatsRepository);
				// stop the chain
				return false;
			}
		}

		// then validate json via parser
		// if it is invalid then we have an exception here
		try {
			CollectorRequestPreprocessor.validateJson(requestBody);
		} catch (InvalidCollectorRequestException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(e.getMessage());
			CollectorRequestPreprocessor.logRequest(requestBody, true, hourlyStatsRepository);
			// stop the chain
			return false;
		}

		// we don't write valid logs here since it still has a chance to be invalid (i.e. customer is not enabled)
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// all the requests that are not returned false from preHandle will fall here
		// thus we have logged every loggable (having a customer id field) requests.
		CollectorRequestPreprocessor.logRequest(requestBody, ex != null || response.getStatus() != HttpServletResponse.SC_OK, hourlyStatsRepository);
	}
}
