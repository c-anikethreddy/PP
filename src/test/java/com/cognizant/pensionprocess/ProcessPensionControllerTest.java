package com.cognizant.pensionprocess;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.pensionprocess.controller.PensionProcessController;
import com.cognizant.pensionprocess.exception.PensionerDetailNotFoundException;
import com.cognizant.pensionprocess.model.AuthResponse;
import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.service.PensionProcessServiceImpl;

@AutoConfigureMockMvc
@SpringBootTest
public class ProcessPensionControllerTest {

	@InjectMocks
	private PensionProcessController controller;
	
	@Mock
	private Environment env;

	@Mock
	private PensionProcessServiceImpl pensionProcessServiceImpl;
	
	@Autowired
	MockMvc mockMvc;
	
	String token;
	PensionerInput pensionerInput;
	ProcessPensionInput processPensionInput;
	PensionDetail pensionDetail;
	

	@SuppressWarnings("deprecation")
	@BeforeEach
	void init() {
		
		token = "token";
		pensionerInput = new PensionerInput("satyam", new Date("01/01/2000"), "ASDFG1234D", Long.parseLong("123123123123"), "self");
		processPensionInput = new ProcessPensionInput(Long.parseLong("123456789123"), 5000.0);
		pensionDetail  = new PensionDetail("satyam", new Date("01/01/2000"), "ASDFG1234D", "self", 5000.0);
		when(pensionProcessServiceImpl.pensionCalculate(pensionerInput)).thenReturn(pensionDetail);
		when(pensionProcessServiceImpl.responseCode(processPensionInput)).thenReturn(10);
	}
	
	@Test
	void getPensionDetailTestUnauthorised() throws Exception {
		when(pensionProcessServiceImpl.hasPermission(token)).thenReturn(new AuthResponse(false));
		mockMvc.perform(post("/pps/pensiondetail")).andReturn();
		ResponseEntity<?> responseEntity = controller.getPensionDetail(token, pensionerInput);
		Assertions.assertEquals(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, responseEntity.getStatusCode());
	}

	
	@Test
	void getProcessingCodeTestUnauthorised() throws Exception {
		when(pensionProcessServiceImpl.hasPermission(token)).thenReturn(new AuthResponse(false));
		mockMvc.perform(post("/pps/processpension")).andReturn();
		ResponseEntity<?> responseEntity = controller.getProcessingCode(token, processPensionInput);
		Assertions.assertEquals(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, responseEntity.getStatusCode());
	}
	
	
	@Test
	void getPensionDetailNotFoundTest() throws Exception {
		when(pensionProcessServiceImpl.hasPermission(token)).thenReturn(new AuthResponse(true));
		mockMvc.perform(post("/pps/pensiondetail")).andReturn();
		when(pensionProcessServiceImpl.pensionCalculate(pensionerInput)).thenThrow(PensionerDetailNotFoundException.class);
		Assertions.assertAll( env.getProperty("27"), () -> {controller.getPensionDetail(token, pensionerInput);});
	}
	
	@Test
	void getProcessingCodeTest() throws Exception {
		when(pensionProcessServiceImpl.hasPermission(token)).thenReturn(new AuthResponse(true));
		mockMvc.perform(post("/pps/processpension")).andReturn();
		ResponseEntity<?> responseEntity = controller.getProcessingCode(token, processPensionInput);
		Assertions.assertEquals(new ResponseEntity<String>(env.getProperty("10"), HttpStatus.OK), responseEntity);
	}
}
