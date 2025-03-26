package com.prepaidplus.mobicomm.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @Column(nullable = false)
    private String validity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private PlanCategory category;

    @Column(nullable = false)
    private Double price;
    
    private String data; 
    private String voice;
    private String sms;
    private String network;
    private String talktime;
    private String note;
    private boolean active = true;
}
