package com.twinline.loan_management.controller;

import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.exceptions.UnauthorizedException;
import com.twinline.loan_management.service.LoanApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/loanmanagement/application")
@Slf4j
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Value("${app.security.enabled}")
    private boolean securityEnabled;

    @PostMapping("/new")
    public ResponseEntity<?> submitLoan(@RequestBody LoanApplication loan, Principal principal) {
        if (securityEnabled && principal == null) {
            throw new UnauthorizedException("Please login first");
        }

        if (loan.getApplicantName() == null || loan.getLoanAmount() == null ||
                loan.getTenure() == null || loan.getIncome() == null ||
                loan.getContactDetails() == null) {
            return ResponseEntity.badRequest().body("All fields are required");
        }

        LoanApplication savedLoan = loanApplicationService.createLoan(loan);
        return ResponseEntity.ok(savedLoan);
    }
}

