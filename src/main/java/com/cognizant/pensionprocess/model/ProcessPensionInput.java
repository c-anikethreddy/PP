package com.cognizant.pensionprocess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Nayan, Akshita, Akhil
 * Entity/model class for PensionDisbursement ProcessPensionInput
 * USe of Lombok for default constructor,parameterized constructor and getters and setters
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ProcessPensionInput {
	/**
	 * instance variables for ProcessPensionInput entity class
	 */


	/**  Aadhaar Number*/
	private Long aadharNumber;
	
	/** Pension Amount*/
	private Double pensionAmount;
	
}
