package com.twinline.loan_management.service;

import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.exceptions.ResourceNotFoundException;
import com.twinline.loan_management.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    public List<LoanApplication> getAllLoans() {
        List<LoanApplication> loans = loanApplicationRepository.findAll();
        if (loans.isEmpty()) {
            throw new ResourceNotFoundException("No loan applications found");
        }
        return loans;
    }

    public LoanApplication getLoanById(Long id) {
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    public LoanApplication createLoan(LoanApplication loanApplication) {
        if (loanApplication == null) {
            throw new ResourceNotFoundException("Loan application cannot be null");
        }
        loanApplication.setStatus("Pending");
        loanApplication.setCreatedAt(LocalDateTime.now());
        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication updateLoan(LoanApplication loanApplication) {
        if (loanApplication == null) {
            throw new ResourceNotFoundException("Loan application cannot be null");
        }
        if (loanApplication.getId() == null) {
            throw new ResourceNotFoundException("Loan application ID cannot be null for update");
        }

        loanApplicationRepository.findById(loanApplication.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanApplication.getId()));

        return loanApplicationRepository.save(loanApplication);
    }
}