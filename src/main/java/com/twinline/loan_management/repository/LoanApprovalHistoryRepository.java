package com.twinline.loan_management.repository;

import com.twinline.loan_management.entities.LoanApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApprovalHistoryRepository extends JpaRepository<LoanApprovalHistory, Long> {
    List<LoanApprovalHistory> findByLoanApplicationIdOrderByUpdatedAtDesc(Long loanApplicationId);
}