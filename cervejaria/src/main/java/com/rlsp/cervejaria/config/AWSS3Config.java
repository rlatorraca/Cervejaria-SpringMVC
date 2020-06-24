package com.rlsp.cervejaria.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

@Configuration
@PropertySource(value = { "file://${HOME}/.cervejaria-s3.properties" }, ignoreResourceNotFound = true)
public class AWSS3Config {
	
	@Autowired
	private Environment env;

	@Bean
	public AmazonTextract amazonS3() {
	//public AmazonS3 amazonS3() {
		///AWSCredentials credenciais = new BasicAWSCredentials(	env.getProperty("AWS_ACCESS_KEY_ID"), env.getProperty("AWS_SECRET_ACCESS_KEY"));
		BasicAWSCredentials credenciais = new BasicAWSCredentials(env.getProperty("AWS_ACCESS_KEY_ID"), env.getProperty("AWS_SECRET_ACCESS_KEY"));
		//AmazonS3 amazonS3 = new AmazonS3Client(credenciais, new ClientConfiguration());
				
		Region regiao = Region.getRegion(Regions.US_EAST_1);
		AmazonTextract amazonS3 = AmazonTextractClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credenciais))
                .withRegion(Regions.US_EAST_1)
                .build();
//		AmazonS3 amazonS3 = AmazonS3Client.builder()
//					.withRegion("us-east-1")
//					.withCredentials(new AWSStaticCredentialsProvider(credenciais))					
//					.build();
//		amazonS3.setRegion(regiao);
		return amazonS3;
	}
	
}
