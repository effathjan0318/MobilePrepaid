package com.prepaidplus.mobicomm.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true, nullable = false)
    private String categoryName; // "Data Pack", "Unlimited", etc.
}
