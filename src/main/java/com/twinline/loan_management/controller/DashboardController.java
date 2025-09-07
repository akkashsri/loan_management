package com.twinline.loan_management.controller;

import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.exceptions.UnauthorizedException;
import com.twinline.loan_management.service.LoanApplicationService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/loanmanagement")
@Slf4j
public class DashboardController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Value("${app.security.enabled}")
    private boolean securityEnabled;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Principal principal) {
        if (securityEnabled && principal == null) {
            throw new UnauthorizedException("Please login first");
        }

        List<LoanApplication> loans = loanApplicationService.getAllLoans();
        return ResponseEntity.ok(loans);
    }
}
