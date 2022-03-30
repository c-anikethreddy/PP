package com.cognizant.pensionprocess;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cognizant.pensionprocess.model.BankDetail;
import com.cognizant.pensionprocess.model.PensionDetail;
import com.cognizant.pensionprocess.model.PensionerDetail;
import com.cognizant.pensionprocess.model.PensionerInput;
import com.cognizant.pensionprocess.model.ProcessPensionInput;
import com.cognizant.pensionprocess.model.ProcessPensionResponse;

import nl.jqno.equalsverifier.EqualsVerifier;

@SpringBootTest
class PensionProcessMicroserviceApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	public void testPensionerDetailsEquals() {
		EqualsVerifier.simple().forClass(PensionerDetail.class).verify();
	}
	
	@Test
	public void testBankDetailsEquals() {
		EqualsVerifier.simple().forClass(BankDetail.class).verify();
	}
	
	@Test
	public void testPensionerInputEquals() {
		EqualsVerifier.simple().forClass(PensionerInput.class).verify();
	}
	
	
	@Test
	public void testProcessPensionInputEquals() {
		EqualsVerifier.simple().forClass(ProcessPensionInput.class).verify();
	}
	
	@Test
	public void testProcessPensionResponseEquals() {
		EqualsVerifier.simple().forClass(ProcessPensionResponse.class).verify();
	}
	
	@Test
	public void testProcessPensionDetailsEquals() {
		EqualsVerifier.simple().forClass(PensionDetail.class).verify();
	}
	
	@Test
	public void testMain() {
		PensionProcessMicroserviceApplication.main(new String[] {});
	}

}
