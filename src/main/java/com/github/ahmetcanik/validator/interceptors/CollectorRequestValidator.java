package com.github.ahmetcanik.validator.interceptors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.data.repository.UaBlacklistRepository;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;
import com.github.ahmetcanik.validator.exceptions.UserAgentBlacklistedException;

import java.io.IOException;

public class CollectorRequestValidator {
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
