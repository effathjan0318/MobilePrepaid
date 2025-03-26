package com.prepaidplus.mobicomm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.prepaidplus.mobicomm.utils.SecretKeyGenerator;

@SpringBootApplication
public class MobiCommApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobiCommApplication.class, args);
//		System.out.println("Generated Secret Key: " + SecretKeyGenerator.generateSecretKey());
	}

}
