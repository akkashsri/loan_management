package com.twinline.loan_management.service;

import com.twinline.loan_management.entities.LoanApprovalHistory;
import com.twinline.loan_management.exceptions.ResourceNotFoundException;
import com.twinline.loan_management.repository.LoanApprovalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanApprovalHistoryService {

    @Autowired
    private LoanApprovalHistoryRepository historyRepository;

    public List<LoanApprovalHistory> getHistoryForLoan(Long loanId) {
        if (loanId == null) {
            throw new ResourceNotFoundException("Loan ID cannot be null");
        }
        List<LoanApprovalHistory> history = historyRepository.findByLoanApplicationIdOrderByUpdatedAtDesc(loanId);
        if (history.isEmpty()) {
            throw new ResourceNotFoundException("No history found for loan with id: " + loanId);
        }
        return history;
    }

    public LoanApprovalHistory addHistory(LoanApprovalHistory history) {
        if (history == null) {
            throw new ResourceNotFoundException("History cannot be null");
        }
        return historyRepository.save(history);
    }
}