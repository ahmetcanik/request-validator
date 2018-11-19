package com.github.ahmetcanik.validator.interceptors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import com.github.ahmetcanik.validator.data.entity.LatestStats;
import com.github.ahmetcanik.validator.data.repository.HourlyStatsRepository;
import com.github.ahmetcanik.validator.data.repository.UaBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.exceptions.UserAgentBlacklistedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectorRequestPreprocessor {
	static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void logRequest(String requestBody, boolean isInvalidRequest, HourlyStatsRepository hourlyStatsRepository) {
		// we use regex, not json parser
		// in case of malformed json
		Pattern pattern = Pattern.compile("\"customerId\"\\s*:\\s*(\\d+)");
		Matcher matcher = pattern.matcher(requestBody);
		if (matcher.find()) {
			int customerId;
			try {
				customerId = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException e) {
				logger.warn("Not a valid customerId extracted from request. Request would not be charged.");
				return;
			}
			logRequest(customerId, isInvalidRequest, hourlyStatsRepository);
		} else
			logger.warn("customerId couldn't be extracted from request. Request would not be charged.");
	}

	public static void logRequest(int customerId, boolean isInvalidRequest, HourlyStatsRepository hourlyStatsRepository) {
		// TODO make atomic
		// get latest statistics from database
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp currentHour = new Timestamp(calendar.getTime().getTime());
		List<HourlyStats> latestStats = hourlyStatsRepository.findByCustomerIdAndTime(customerId, currentHour);
		HourlyStats newHourlyStats;
		// first request for the hour
		if (latestStats.isEmpty()) {
			newHourlyStats = new HourlyStats();
			newHourlyStats.setCustomerId(customerId);
			newHourlyStats.setTime(currentHour);
		}
		else
			newHourlyStats = latestStats.get(0);

		newHourlyStats.setRequestCount(newHourlyStats.getRequestCount() + 1);// increment
		if (isInvalidRequest)
			newHourlyStats.setInvalidCount(newHourlyStats.getInvalidCount() + 1);// increment
		// save new stat
		hourlyStatsRepository.save(newHourlyStats);
	}

	public static void validateJson(String collectorRequestJson) throws InvalidCollectorRequestException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(collectorRequestJson);
			if (jsonNode == null)
				throw new InvalidCollectorRequestException("Malformed JSON");
		} catch (IOException e) {
			throw new InvalidCollectorRequestException("Malformed JSON", e);
		}
	}

	public static void validateUserAgent(String userAgent, UaBlacklistRepository uaBlacklistRepository) throws UserAgentBlacklistedException {
		if (uaBlacklistRepository.existsById(userAgent))
			throw new UserAgentBlacklistedException("User-Agent " + userAgent + " is blacklisted");
	}
}
