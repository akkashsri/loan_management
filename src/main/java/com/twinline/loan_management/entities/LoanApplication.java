package com.twinline.loan_management.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;

    private Double loanAmount;

    private Integer tenure;

    private Double income;

    private String contactDetails;

    private String status;

    private LocalDateTime createdAt;
}
