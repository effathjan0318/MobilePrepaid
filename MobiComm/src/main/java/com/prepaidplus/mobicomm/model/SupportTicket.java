package com.prepaidplus.mobicomm.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "support_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String issueType; // "Network Issue", "Billing", etc.
    private String description;
    private String status = "Open"; // Default: Open

    private String createdAt;
    private String updatedAt;
}

