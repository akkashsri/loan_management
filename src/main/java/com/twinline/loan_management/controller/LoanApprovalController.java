package com.twinline.loan_management.controller;

import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.entities.LoanApprovalHistory;
import com.twinline.loan_management.exceptions.UnauthorizedException;
import com.twinline.loan_management.service.LoanApplicationService;
import com.twinline.loan_management.service.LoanApprovalHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/loanmanagement/loan")
@Slf4j
public class LoanApprovalController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private LoanApprovalHistoryService historyService;

    @Value("${app.security.enabled}")
    private boolean securityEnabled;

    @GetMapping("/{id}")
    public ResponseEntity<?> viewLoanDetails(@PathVariable("id") Long id, Principal principal) {
        if (securityEnabled && principal == null) {
            throw new UnauthorizedException("Please login first");
        }

        LoanApplication loan = loanApplicationService.getLoanById(id);
        List<LoanApprovalHistory> historyList = historyService.getHistoryForLoan(id);

        return ResponseEntity.ok(Map.of(
                "loan", loan,
                "history", historyList
        ));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<String> updateLoanStatus(
            @PathVariable("id") Long id,
            @RequestParam("step") String step,
            @RequestParam("status") String status,
            @RequestParam(value = "comment", required = false) String comment,
            Principal principal) {

        if (securityEnabled && principal == null) {
            throw new UnauthorizedException("Please login first");
        }

        if (step == null || step.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Step is required");
        }

        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Status is required");
        }

        LoanApplication loan = loanApplicationService.getLoanById(id);
        loan.setStatus(status);
        loanApplicationService.updateLoan(loan);

        LoanApprovalHistory history = new LoanApprovalHistory();
        history.setLoanApplication(loan);
        history.setStep(step);
        history.setStatus(status);
        history.setRemarks(comment);
        history.setUpdatedAt(LocalDateTime.now());

        historyService.addHistory(history);

        return ResponseEntity.ok("Loan status updated successfully");
    }
}

