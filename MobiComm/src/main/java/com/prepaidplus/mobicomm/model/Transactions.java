package com.prepaidplus.mobicomm.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "recharge_id")
    private Recharge recharge;

    private Double amount;
    private String paymentMethod; 
    private String status = "Pending"; 

    private LocalDateTime timestamp;
}

