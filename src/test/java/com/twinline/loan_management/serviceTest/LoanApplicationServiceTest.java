package com.twinline.loan_management.serviceTest;

import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.exceptions.ResourceNotFoundException;
import com.twinline.loan_management.repository.LoanApplicationRepository;
import com.twinline.loan_management.service.LoanApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    private LoanApplication loan;

    @BeforeEach
    void setUp() {
        loan = new LoanApplication();
        loan.setId(1L);
        loan.setApplicantName("John Doe");
        loan.setStatus("Pending");
        loan.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetLoanById_Success() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoanApplication foundLoan = loanApplicationService.getLoanById(1L);

        assertNotNull(foundLoan);
        assertEquals("John Doe", foundLoan.getApplicantName());
        verify(loanApplicationRepository).findById(1L);
    }

    @Test
    void testGetLoanById_NotFound() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            loanApplicationService.getLoanById(1L);
        });
        verify(loanApplicationRepository).findById(1L);
    }

    @Test
    void testGetAllLoans_Success() {
        when(loanApplicationRepository.findAll()).thenReturn(List.of(loan));

        List<LoanApplication> loans = loanApplicationService.getAllLoans();

        assertFalse(loans.isEmpty());
        assertEquals(1, loans.size());
        verify(loanApplicationRepository).findAll();
    }

    @Test
    void testGetAllLoans_NoLoansFound() {
        when(loanApplicationRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            loanApplicationService.getAllLoans();
        });
        verify(loanApplicationRepository).findAll();
    }

    @Test
    void testCreateLoan_Success() {
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loan);

        LoanApplication newLoan = new LoanApplication();
        LoanApplication savedLoan = loanApplicationService.createLoan(newLoan);

        assertNotNull(savedLoan);
        assertEquals("Pending", savedLoan.getStatus());
        assertNotNull(savedLoan.getCreatedAt());
        verify(loanApplicationRepository).save(any(LoanApplication.class));
    }

    @Test
    void testUpdateLoan_Success() {
        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loan);

        loan.setStatus("Approved");
        LoanApplication updatedLoan = loanApplicationService.updateLoan(loan);

        assertNotNull(updatedLoan);
        assertEquals("Approved", updatedLoan.getStatus());
        verify(loanApplicationRepository).findById(1L);
        verify(loanApplicationRepository).save(any(LoanApplication.class));
    }
}
