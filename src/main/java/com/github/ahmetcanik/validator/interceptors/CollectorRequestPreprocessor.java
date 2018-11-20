package com.github.ahmetcanik.validator.interceptors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.data.entity.HourlyStats;
import com.github.ahmetcanik.validator.data.repository.HourlyStatsRepository;
import com.github.ahmetcanik.validator.data.repository.UaBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.exceptions.UserAgentBlacklistedException;
import com.github.ahmetcanik.validator.services.entity.CollectorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectorRequestPreprocessor {
	static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	static ObjectMapper objectMapper = new ObjectMapper();

	public synchronized static void logRequest(String requestBody, boolean isInvalidRequest, HourlyStatsRepository hourlyStatsRepository) {
		// first try json parser assuming json is not malformed
		CollectorRequest collectorRequest = null;
		try {
			collectorRequest = objectMapper.readValue(requestBody, CollectorRequest.class);
			Timestamp time = new Timestamp(collectorRequest.getTimestamp());
			logRequest(collectorRequest.getCustomerID(), time, isInvalidRequest, hourlyStatsRepository);
			// we've done logging
			return;
		} catch (IOException e) {
			logger.warn("Request is malformed, falling back to regex parsing to extract customer id");
		}

		// falling back to regex parser
		// in case of malformed json
		Pattern pattern = Pattern.compile("\"customerID\"\\s*:\\s*(\\d+)");
		Matcher matcher = pattern.matcher(requestBody);
		if (matcher.find()) {
			int customerId;
			try {
				customerId = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException e) {
				logger.warn("Not a valid customerID extracted from request. Request would not be charged.");
				return;
			}
			// don't try to parse timestamp from request since it would not be trustworthy
			// just assume it as now
			Timestamp now = Timestamp.from(Instant.now());
			logRequest(customerId, now, isInvalidRequest, hourlyStatsRepository);
		} else
			logger.warn("customerID couldn't be extracted from request. Request would not be charged.");
	}

	private static void logRequest(int customerId, Timestamp time, boolean isInvalidRequest, HourlyStatsRepository hourlyStatsRepository) {
		// TODO make atomic
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.getTime());
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp currentHour = new Timestamp(calendar.getTime().getTime());

		// get latest statistics from database
		List<HourlyStats> latestStats = hourlyStatsRepository.findByCustomerIdAndTime(customerId, currentHour);
		HourlyStats newHourlyStats;
		// first request for the hour
		if (latestStats.isEmpty()) {
			newHourlyStats = new HourlyStats();
			newHourlyStats.setCustomerId(customerId);
			newHourlyStats.setTime(currentHour);
		} else
			newHourlyStats = latestStats.get(0);

		newHourlyStats.setRequestCount(newHourlyStats.getRequestCount() + 1);// increment
		if (isInvalidRequest)
			newHourlyStats.setInvalidCount(newHourlyStats.getInvalidCount() + 1);// increment
		// save new stat
		hourlyStatsRepository.save(newHourlyStats);
	}

	public static void validateJson(String collectorRequestJson) throws InvalidCollectorRequestException {
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
