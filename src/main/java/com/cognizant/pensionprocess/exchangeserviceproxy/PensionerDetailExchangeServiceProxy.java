package com.cognizant.pensionprocess.exchangeserviceproxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cognizant.pensionprocess.model.PensionerDetail;

/**
 * @author Nayan, Akshita, Akhil
 * 
 * PensionerDetailExchangeServiceProxy is a feign Client interface to fetch details
 *  from PensionerDetail micro service on basis of its aadhaar 
 * annotation @FeignClient - passing name of microservice and its url
 *
 */
//@FeignClient(name = "Pensioner-detail", url="http://localhost:9191")
@FeignClient(name = "Pensioner-detail", url="http://pensionerdetailmicroservice-env.us-east-1.elasticbeanstalk.com")
public interface PensionerDetailExchangeServiceProxy {

	/**
	 * getPensionerDetailByAadhaar() requests Pensioner Details
	 * @param aadhaarNumber
	 * @return PensionerDetail- details of Pensioner
	 */
	@PostMapping("/pds/pensionerdetailbyaadhaar")
	//@PostMapping("/pensionerdetailbyaadhaar")
		ResponseEntity<PensionerDetail> getPensionerDetailByAadhaar(@RequestBody String aadhaarNumber);

}
