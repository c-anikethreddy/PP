package com.cognizant.pensionprocess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.cognizant.pensionprocess.exception.PensionerDetailNotFoundException;
import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.service.PensionProcessServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import com.cognizant.pensionprocess.model.AuthResponse;

/**
 *  
 * @author @author Neelima, Ramya, Aniketh, Satya
 * 
 * PensionProcessController is controller class to check for url mappings of pension process micro service
 * Annotated with @RestController for creating Restful controller.
 */


@RestController
@PropertySource("classpath:messages.properties")
@Slf4j
public class PensionProcessController {

	/**
	 * Autowired Environment for fetching properties form properties file
	 */
	@Autowired
	private Environment environment;

	/**
	 * Autowired to PensionProcessService Interface
	 */
	@Autowired
	private PensionProcessServiceImpl pensionService;


	/**
	 * getPensionDetail() call pensionCalculate method to calculate pension details
	 * of pensioner through post mapping
	 * 
	 * @param token
	 * @param pensionerInput
	 * @return pension detail if http status is OK else return bad request
	 * @throws PensionerDetailNotFoundException
	 */
	

	@PostMapping("/pensiondetail")
	@HystrixCommand(fallbackMethod = "getFallbackPensionDetail")
	public ResponseEntity<?> getPensionDetail(@RequestHeader(name = "Authorization") String token,
			@RequestBody final PensionerInput pensionerInput) {
		log.info("START :: Method :: getPensionDetail() :: ");

		log.info("controller started");
		log.debug("date: " + pensionerInput.getDateOfBirth());
		AuthResponse valid  = pensionService.hasPermission(token);
		if(!valid.isValid()){
			String response = "40";
			log.info("END :: Method :: getPensionDetail() :: ");
			return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
		} 

		log.debug("authorised");
		try {
			PensionDetail pensionDetail = pensionService.pensionCalculate(pensionerInput);
           
			log.debug(pensionDetail.toString());
			log.info("END :: Method :: getPensionDetail() :: ");
			return new ResponseEntity<PensionDetail>(pensionDetail, HttpStatus.OK);
		} catch (PensionerDetailNotFoundException exception) {
			log.debug("bad request");
			String response = "27";
			log.info("END :: Method :: getPensionDetail() :: ");
			return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.BAD_REQUEST);
		}
		
	}

	/** 
	 * Fallback method for pension calculation if that method is unavailable
	 * @param pensionerInput
	 * @return string
	 */
	public ResponseEntity<?> getFallbackPensionDetail(@RequestHeader(name = "Authorization") String token,
			@RequestBody final PensionerInput pensionerInput) {
		log.debug("service unavailable");
		String response = "25";
		return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * getProcessingCode() return the response code for disbursement of pension
	 * It takes processInput as parameter that has aadhaar and pension amount and return status code
	 *  
	 *  @param processInput
	 *  @return string 
	 */
	

	@PostMapping("/processpension")
	@HystrixCommand(fallbackMethod = "getFallbackProcessingCode")
	public ResponseEntity<?> getProcessingCode(@RequestHeader(name = "Authorization") String token,@RequestBody final ProcessPensionInput processInput) {
		log.info("START :: Method :: getProcessingCode() :: ");

		AuthResponse valid  = pensionService.hasPermission(token);
		if(!valid.isValid()){
			String response = "40";
			return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
		}
		int statusCode = pensionService.responseCode(processInput);
		String response = Integer.toString(statusCode);
		log.info("END :: Method :: getProcessingCode() :: ");
		return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.OK);
	}

	/**
	 *  Fallback method for pension processing code if that method is unavailable
	 *  @param processInput
	 *  @return String
	 */
	public ResponseEntity<?> getFallbackProcessingCode(@RequestHeader(name = "Authorization") String token,
			@RequestBody final ProcessPensionInput processInput) {
		String response = "30";
		return new ResponseEntity<String>(environment.getProperty(response), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
