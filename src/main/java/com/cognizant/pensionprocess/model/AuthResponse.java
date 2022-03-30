package com.cognizant.pensionprocess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Nayan, Akshita, Akhil
 * Entity class for AuthResponse 
 * Use of lombok for getters , setters and constructors
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {

	/**
	 * instance variable isValid returns the boolean value that whether token is valid or not
	 */
	private boolean isValid;

}