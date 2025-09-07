package com.twinline.loan_management.serviceTest;

import com.twinline.loan_management.entities.LoanApprovalHistory;
import com.twinline.loan_management.exceptions.ResourceNotFoundException;
import com.twinline.loan_management.repository.LoanApprovalHistoryRepository;
import com.twinline.loan_management.service.LoanApprovalHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApprovalHistoryServiceTest {

    @Mock
    private LoanApprovalHistoryRepository historyRepository;

    @InjectMocks
    private LoanApprovalHistoryService historyService;

    @Test
    void testGetHistoryForLoan_Success() {
        LoanApprovalHistory history = new LoanApprovalHistory();
        history.setId(1L);
        history.setStep("Initial Check");
        when(historyRepository.findByLoanApplicationIdOrderByUpdatedAtDesc(1L)).thenReturn(List.of(history));

        List<LoanApprovalHistory> result = historyService.getHistoryForLoan(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(historyRepository).findByLoanApplicationIdOrderByUpdatedAtDesc(1L);
    }

    @Test
    void testGetHistoryForLoan_NotFound() {
        when(historyRepository.findByLoanApplicationIdOrderByUpdatedAtDesc(1L)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            historyService.getHistoryForLoan(1L);
        });
        verify(historyRepository).findByLoanApplicationIdOrderByUpdatedAtDesc(1L);
    }

    @Test
    void testAddHistory_Success() {
        LoanApprovalHistory historyToSave = new LoanApprovalHistory();
        historyToSave.setStep("Final Approval");

        when(historyRepository.save(any(LoanApprovalHistory.class))).thenReturn(historyToSave);

        LoanApprovalHistory savedHistory = historyService.addHistory(historyToSave);

        assertNotNull(savedHistory);
        assertEquals("Final Approval", savedHistory.getStep());
        verify(historyRepository).save(any(LoanApprovalHistory.class));
    }
}
