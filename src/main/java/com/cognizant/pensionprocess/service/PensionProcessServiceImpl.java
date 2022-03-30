package com.cognizant.pensionprocess.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cognizant.pensionprocess.exception.AccessDeniedException;
import com.cognizant.pensionprocess.exception.PensionerDetailNotFoundException;
import com.cognizant.pensionprocess.exchangeserviceproxy.AuthClient;
import com.cognizant.pensionprocess.exchangeserviceproxy.PensionDisbursementExchangeServiceProxy;
import com.cognizant.pensionprocess.exchangeserviceproxy.PensionerDetailExchangeServiceProxy;
import com.cognizant.pensionprocess.model.AuthResponse;
import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.model.ProcessPensionResponse;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * @author Nayan, Akshita, Akhil
 * 
 * PensionProcessServiceImpl is the implementation class for PensionProcessService interface
 * It is service class that has the implementation for pensionCalculate method and responseCode method
 * Annotated with @Service -provide some business functionalities
 */
@Service
@Slf4j
@PropertySource("classpath:messages.properties")
public class PensionProcessServiceImpl implements PensionProcessService {

	/**
	 * environment reference of Environment is autowired that read properties from messages.properties file
	 */
	@Autowired
	private Environment environment;
	
	@Autowired
	AuthClient authClient;

	/**
	 * pensionerDetailProxy reference of PensionerDetailExchangeServiceProxy Autowired 
	 */
	@Autowired
	private PensionerDetailExchangeServiceProxy pensionerDetailProxy;

	/**
	 *  pensionDisbursementProxy reference of PensionDisbursementExchangeServiceProxy is autowired
	 */
	@Autowired
	private PensionDisbursementExchangeServiceProxy pensionDisbursementProxy;

	/**
	 * This method checks that if the token is valid or not
	 * @param token
	 * @return AuthResponse
	 */
	public AuthResponse hasPermission(String token) {
		log.info("START :: Method :: hasPermission() :: ");

		ResponseEntity<AuthResponse> authResponse = new ResponseEntity<AuthResponse>(HttpStatus.UNAUTHORIZED);
		try {
			authResponse = authClient.getValidity(token);
			log.debug(token);
			System.out.println(authResponse);
			if (!authResponse.getBody().isValid()) {
				throw new AccessDeniedException();
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new AccessDeniedException();
		}
		log.info("END :: Method :: hasPermission() :: ");
		return authResponse.getBody();
	}
	/**
	 * pensionCalculate() takes the parameter pensionerInput as an input , 
	 * calculate the pension and return the detail as PensionerDetail
	 * 
	 * @param pensionerInput
	 * @return PensionerDetail
	 */
	@Override
	public PensionDetail pensionCalculate(final PensionerInput pensionerInput) {
		log.info("START :: Method :: pensionCalculate() :: ");
		ResponseEntity<PensionerDetail> pensionerDetailEntity = null;

		// checks for Http Response 400
		try {
			log.debug("" + pensionerInput.getAadharNumber());
			System.out.println("hi");
			pensionerDetailEntity = pensionerDetailProxy
					.getPensionerDetailByAadhaar(Long.toString(pensionerInput.getAadharNumber()));
			System.out.println("hi11111");
			if (pensionerDetailEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
				
				throw new PensionerDetailNotFoundException();
			}
		} catch (FeignException.BadRequest e) {
			
			throw new PensionerDetailNotFoundException();
		}
		
		PensionerDetail pensionerDetail = pensionerDetailEntity.getBody();

		// Time Zone Correction

		Calendar cal = Calendar.getInstance();
		cal.setTime(pensionerInput.getDateOfBirth());
		cal.add(Calendar.MINUTE, 0);
		Date date = cal.getTime();

		pensionerInput.setDateOfBirth(date);

		if (!pensionerInput.getName().equals(pensionerDetail.getName())
				|| !pensionerInput.getPan().equals(pensionerDetail.getPan())
				|| !pensionerInput.getPensionType().equals(pensionerDetail.getPensionType())
				|| !pensionerInput.getDateOfBirth().equals(pensionerDetail.getDateOfBirth())) {
			throw new PensionerDetailNotFoundException();
		}

		double salary = pensionerDetail.getSalaryEarned();
		double allowances = pensionerDetail.getAllowances();
		String pensionType = pensionerDetail.getPensionType();
		String bankType = pensionerDetail.getBankDetail().getBankType();
		double pensionAmount = 0.0;
		Double bankCharges;

		log.debug(Double.toString(salary));
		log.debug(Double.toString(allowances));
		log.debug(pensionType);
		log.debug(bankType);
		log.debug(environment.getProperty("bank.type2"));

		if (bankType.equalsIgnoreCase(environment.getProperty("bank.type1"))) {
			bankCharges = pensionDisbursementProxy.getServiceCharge(bankType);
		} else if (bankType.equalsIgnoreCase(environment.getProperty("bank.type2"))) {
			bankCharges = pensionDisbursementProxy.getServiceCharge(bankType);
		} else {
			throw new PensionerDetailNotFoundException();
		}
		log.debug(environment.getProperty("message.service.charge")+bankCharges);

		if (pensionType.equalsIgnoreCase(environment.getProperty("pension.type1")) && bankCharges!=0.0) {
			pensionAmount = 0.8 * salary + allowances - bankCharges;
		} else if (pensionType.equalsIgnoreCase(environment.getProperty("pension.type2")) && bankCharges!=0.0) {
			pensionAmount = 0.5 * salary + allowances - bankCharges;
		} else {
			throw new PensionerDetailNotFoundException();
		}
		log.debug(environment.getProperty("message.pension.amount") + pensionAmount);
		log.info("END :: Method :: pensionCalculate() :: ");
		// Time Zone Correction

				cal = Calendar.getInstance();
				cal.setTime(pensionerInput.getDateOfBirth());
				cal.add(Calendar.MINUTE, +330);
				date = cal.getTime();
				pensionerInput.setDateOfBirth(date);
				
		return new PensionDetail(pensionerDetail.getName(), pensionerInput.getDateOfBirth(),
				pensionerDetail.getPan(), pensionerDetail.getPensionType(), pensionAmount);

	}

	/**
	 * responseCode will return response for inititating pension disburse
	 * If status is 10, pension is disbursed else not
	 * 
	 * @param processInput
	 * @return pensionStatusCode
	 */
	@Override
	public int responseCode(final ProcessPensionInput processInput) {
		log.info("START :: Method :: responseCode() :: ");
		ResponseEntity<ProcessPensionResponse> processingCode = pensionDisbursementProxy
				.getResponseCode(processInput);
		
		ProcessPensionResponse response = processingCode.getBody();
		log.debug(environment.getProperty("message.status") + response.getPensionStatusCode());
		log.info("END :: Method :: responseCode() :: ");
		return response.getPensionStatusCode();

	}

}
