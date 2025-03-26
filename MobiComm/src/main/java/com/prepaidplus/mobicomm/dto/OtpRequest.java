package com.prepaidplus.mobicomm.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
	
	 private String phoneNumber;
	    private String email;
	    private String otp;

}
