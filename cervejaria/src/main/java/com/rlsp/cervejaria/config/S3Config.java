package com.rlsp.cervejaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Profile("prod")
@Configuration
@PropertySource(value = { "file://${HOME}/.cervejaria-s3.properties" }, ignoreResourceNotFound = true)
public class S3Config {

	@Autowired
	private Environment env;

	
	  @Bean public AmazonS3 amazonS3() {
	  
	  BasicAWSCredentials credenciais = new  BasicAWSCredentials(env.getProperty("AWS_ACCESS_KEY"),
	  env.getProperty("AWS_SECRET_ACCESS_KEY"));
	  
	  //String regiao = Region.getRegion(Regions.US_EAST_1); 
		/*
		 * AmazonTextract amazonS3 = AmazonTextractClientBuilder.standard()
		 * .withCredentials(new AWSStaticCredentialsProvider(credenciais))
		 * .withRegion(Regions.US_EAST_1) .build();
		 */ 
	  AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
			  .withRegion(Regions.US_EAST_1) 
			  .withCredentials(new AWSStaticCredentialsProvider(credenciais)) 
			  .build();
	  System.out.println(amazonS3.getRegionName());
	  System.out.println(amazonS3.getUrl("cervejaria-rlsp", null));
	  return amazonS3; 
	  }
	 

	
}
