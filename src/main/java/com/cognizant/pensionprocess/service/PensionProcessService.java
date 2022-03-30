package com.cognizant.pensionprocess.service;

import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;

/**
 * @author Nayan, Akshita, Akhil
 * PensionProcessService Interface for PensionProcess microservice
 *
 */
public interface PensionProcessService {

	/**
	 * pensionCalculate() calculates pension and return pension details
	 * 
	 * @param pensionerInput
	 * @return detail of pension of pensioner
	 */
	public PensionDetail pensionCalculate(PensionerInput pensionerInput);

	/**
	 * verifies pension amount and disburse pension when data is valid
	 * 
	 * @param processPensionInput
	 * @return int-response code
	 */
	public int responseCode(ProcessPensionInput processInput);

}
