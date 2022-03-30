package com.cognizant.pensionprocess.exchangeserviceproxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.pensionprocess.model.AuthResponse;

/**
 * @author Nayan, Akshita, Akhil
 * 
 * AuthClient is a feign Client interface to get validity of the token 
 * annotation @FeignClient - passing name of microservice and its url
 *
 */
//@FeignClient(name = "authorization-service", url = "http://localhost:9696")
@FeignClient(name = "authorization-service", url = "http://authorizationservice-env.us-east-1.elasticbeanstalk.com")
public interface AuthClient {

	/**
	 * This method request to get the validity of token
	 * @param token
	 * @return response - boolean
	 */
	@GetMapping("/auth/validate")
	//@GetMapping("/validate")
	public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization") String token) ;
}