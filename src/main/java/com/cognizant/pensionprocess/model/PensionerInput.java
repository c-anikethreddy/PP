package com.cognizant.pensionprocess.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Nayan, Akshita, Akhil
 * Entity/model class for PensionerInput
 * USe of Lombok for default constructor,parameterized constructor and getters and setters
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class PensionerInput {
	
	/**
	 * instance variables for Pensioner Input entity class
	 */

	/** Pensioner Name */
	private String name;
	
	/** Pensioner DOB */
	private Date dateOfBirth;
	
	/** Pensioner PAN */
	private String pan;
	
	/** Pensioner Aadhaar Number */
	private long aadharNumber;
	
	/** pension Type */
	private String pensionType;
}
