package com.cognizant.pensionprocess.exchangeserviceproxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.model.ProcessPensionResponse;

/**
 * @author Nayan, Akshita, Akhil
 * 
 * PensionDisbursementExchangeServiceProxy is a feign Client interface to fetch details
 *  from PensionDisbursement micro service 
 * annotation @FeignClient - passing name of microservice and its url
 *
 */
//@FeignClient(name = "Pension-Disbursement" ,url="http://localhost:9292")
@FeignClient(name = "Pension-Disbursement" ,url="http://pensiondisbursementmicroservice-env.us-east-1.elasticbeanstalk.com")
public interface PensionDisbursementExchangeServiceProxy {
	
	/**
	 * getResponseCode() requests for response code
	 * @param processPensionInput
	 * @return ProcessPensionResponse-int code
	 */
	@PostMapping(value = "/pdis/disbursepension", produces = MediaType.APPLICATION_JSON_VALUE)
	//@PostMapping(value = "/disbursepension", produces = MediaType.APPLICATION_JSON_VALUE)
		ResponseEntity<ProcessPensionResponse> getResponseCode(@RequestBody ProcessPensionInput processPensionInput);

	
	/**
	 * getServicecharge() requests for bank charge for providing service
	 * @param bankType
	 * @return Double-bank charge
	 */
	@PostMapping(value = "/pdis/bankservicecharge", produces = MediaType.APPLICATION_JSON_VALUE)
	//@PostMapping(value = "/bankservicecharge", produces = MediaType.APPLICATION_JSON_VALUE)
		Double getServiceCharge(@RequestBody String bankType);

}
