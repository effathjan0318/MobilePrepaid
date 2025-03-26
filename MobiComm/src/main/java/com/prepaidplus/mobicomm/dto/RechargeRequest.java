package com.prepaidplus.mobicomm.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeRequest {
    private Integer userId;
    private Integer planId;
    private Integer validityDays;
    private Double amount;
    private String paymentMethod;
}
