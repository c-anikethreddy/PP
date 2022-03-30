package com.cognizant.pensionprocess;

import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cognizant.pensionprocess.exception.PensionerDetailNotFoundException;
import com.cognizant.pensionprocess.exchangeserviceproxy.AuthClient;
import com.cognizant.pensionprocess.exchangeserviceproxy.PensionDisbursementExchangeServiceProxy;
import com.cognizant.pensionprocess.exchangeserviceproxy.PensionerDetailExchangeServiceProxy;
import com.cognizant.pensionprocess.model.BankDetail;
import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.model.ProcessPensionResponse;
import com.cognizant.pensionprocess.service.PensionProcessServiceImpl;

@SpringBootTest
public class PensionProcessServiceTest {

	@InjectMocks
	PensionProcessServiceImpl pensionProcessImpl;
	@Mock
	PensionDisbursementExchangeServiceProxy pensionDisbursementExchangeServiceProxy;
	@Mock
	PensionerDetailExchangeServiceProxy pensionerDetailExchangeServiceProxy;
	@Mock
	AuthClient authClient;
	@Mock
	Environment environment;
	
	private ResponseEntity<PensionerDetail> pensionerDetail;
	private BankDetail bankDetail;
	private PensionerInput pensionerInput;
	private ProcessPensionInput processPensionInput;
	private PensionDetail pensionDetail;
	private ResponseEntity<ProcessPensionResponse> processingCode;

	
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void init() {
		bankDetail = new BankDetail("HDFC", 5000185, "private");
		pensionerDetail = new ResponseEntity<PensionerDetail>(new PensionerDetail(Long.valueOf("123456789456"),
				"prajesh", new Date("01/02/2018"), "BHMAA1234A", 50000.0, 4000, "self", bankDetail), HttpStatus.OK);
		pensionDetail = new PensionDetail("prajesh", new Date("01/02/2018"), "BHMAA1234A", "self", 43450);
		processPensionInput = new ProcessPensionInput(Long.valueOf("123456789456"), (double) 43450);
		processingCode = new ResponseEntity<ProcessPensionResponse>(new ProcessPensionResponse(21), HttpStatus.OK);
		pensionerInput = new PensionerInput("prajesh", new Date("01/02/2018"), "BHMAA1234A",
				Long.valueOf("123456789456"), "self");

	}

	@Test
	public void testCorrectData1() {
		processingCode.getBody().setPensionStatusCode(10);
		when(pensionDisbursementExchangeServiceProxy.getResponseCode(processPensionInput)).thenReturn(processingCode);
		Assertions.assertEquals(10, pensionProcessImpl.responseCode(processPensionInput));
	}

	@Test
	public void testWrongData1() {
		when(pensionDisbursementExchangeServiceProxy.getResponseCode(processPensionInput)).thenReturn(processingCode);
		Assertions.assertEquals(21, pensionProcessImpl.responseCode(processPensionInput));
	}

	
	@Test
	public void testwrongDOB() {

		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAadharNumber() {

		pensionerDetail = new ResponseEntity<PensionerDetail>(new PensionerDetail(Long.valueOf("123456789456"),
				"prajesh", new Date("01/02/2018"), "BHMAA1234A", 50000.0, 4000, "self", bankDetail),
				HttpStatus.BAD_REQUEST);
		pensionerInput.setAadharNumber(123456);
		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}

	@Test
	public void testwrongName() {

		pensionerInput.setName("mukesh");

		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}

	@Test
	public void testwrongPan() {

		pensionerInput.setPan("QWE123QWE");

		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}

	@Test
	public void testwrongPensionType() {

		pensionerInput.setPensionType("family");

		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}

	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testWrongBankType() {

		bankDetail.setBankType("both");
		pensionerDetail = new ResponseEntity<PensionerDetail>(new PensionerDetail(Long.valueOf("123456789456"),
				"prajesh", new Date("01/02/2018"), "BHMAA1234A", 50000.0, 4000, "self", bankDetail), HttpStatus.OK);
		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});
	}
	
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testWrongPensionType() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(pensionerInput.getDateOfBirth());
		cal.add(Calendar.MINUTE, +330);
		Date date = cal.getTime();

		pensionerInput.setDateOfBirth(date);
		pensionerInput.setPensionType("both");
		pensionDetail.setSelfOrFamilyPension("both");

		pensionerDetail = new ResponseEntity<PensionerDetail>(new PensionerDetail(Long.valueOf("123456789456"),
				"prajesh", new Date("01/02/2018"), "BHMAA1234A", 50000.0, 4000, "both", bankDetail), HttpStatus.OK);
		when(pensionerDetailExchangeServiceProxy
				.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber())))
						.thenReturn(pensionerDetail);
		Assertions.assertThrows(PensionerDetailNotFoundException.class, () -> {
			pensionProcessImpl.pensionCalculate(pensionerInput);
		});		
	}
	
}
