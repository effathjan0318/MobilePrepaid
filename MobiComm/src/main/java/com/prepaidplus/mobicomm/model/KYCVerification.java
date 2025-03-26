package com.prepaidplus.mobicomm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kyc_verifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KYCVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kycId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @Column(nullable = false)
    private String documentType; 

    @Column(unique = true, nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String documentImageUrl;

    @Column(nullable = false)
    private String selfieImageUrl;

    @Column(nullable = false)
    private String status = "Pending"; 

    private String submittedAt;
    private String verifiedAt; 

    private String remarks; 
}
