package com.cognizant.pensionprocess;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author Neelima, Ramya, Aniketh, Satya
 * Main class for Pensioner Detail MicroService
 * Annotated with @SpringBootApplication, @ComponentScan to scan all base packages
 * Annotated with @EnableFeignClients for creating REST API clients
 * Annotated with @EnableCircuitBreaker to set up a fallback in application logic
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
@EnableSwagger2
public class PensionProcessMicroserviceApplication {


	/**
	 * Main function to run the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(PensionProcessMicroserviceApplication.class, args);
	}

	/**
	 * Swagger Docket
	 * @return
	 */
	@Bean
	public Docket swaggerConfiguration()
		{
			return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.cognizant.pensionprocess"))
					.build()
					.apiInfo(apiDetails());
		
		}

	/**
	 * Swagger ApiInfo
	 * @return
	 */
	private ApiInfo apiDetails()
	{
		
		return new ApiInfo(
				"Pension Process Microservice",
				"Microservice Form Pension Management Project",
				"1.0",
				"Free To Use",
				new springfox.documentation.service.Contact("Admin", "", "admin@cognizant.com"),
				"API Licesence",
				"....", Collections.emptyList());
		}
}
		
		