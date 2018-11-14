package com.github.ahmetcanik.validator.interceptors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmetcanik.validator.exceptions.InvalidCollectorRequestException;

import java.io.IOException;

public class CollectorRequestJsonValidator {
	public static void validate(String collectorRequestJson) throws InvalidCollectorRequestException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
			objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			objectMapper.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
			JsonNode jsonNode = objectMapper.readTree(collectorRequestJson);
			if (jsonNode == null)
				throw new InvalidCollectorRequestException("Malformed JSON");
		} catch (IOException e) {
			throw new InvalidCollectorRequestException("", e);
		}
	}
}
