package com.github.ahmetcanik.validator.controller;

import com.github.ahmetcanik.validator.CollectorRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RequestProcessorController.class)
public class RequestProcessorControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RequestProcessorController requestProcessorController;

	@Test
	public void processRequest() {
		CollectorRequest collectorRequest = new CollectorRequest();
		collectorRequest.setCustomerID(1);
		collectorRequest.setTagID(2);
		collectorRequest.setUserID("aaaaaaaa-bbbb-cccc-1111-222222222222");
		collectorRequest.setRemoteIP("123.234.56.78");
		collectorRequest.setTimestamp(1500000000);

		given(requestProcessorController.processRequest(collectorRequest))
				.willReturn(new ResponseEntity<>("OK", HttpStatus.OK));
	}

	@Test
	public void processRequestInvalidContentType() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_XML)
				.content("{")).andExpect(status().isUnsupportedMediaType());
	}

	@Test
	public void processRequestMalformedJSON() throws Exception {
		mvc.perform(post("/processor")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{")).andExpect(status().isBadRequest());
	}
}