package com.cognizant.pensionprocess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;
/**
 * 
 * @author Nayan, Akshita, Akhil
 * PensionerDetailNotFoundException class extends RuntimeException 
 * Will handle exception when PensionerDetail is not found
 */
@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PensionerDetailNotFoundException extends RuntimeException {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
}
