package com.twinline.loan_management.controllerTest;

import com.twinline.loan_management.controller.LoanApprovalController;
import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.entities.LoanApprovalHistory;
import com.twinline.loan_management.service.LoanApplicationService;
import com.twinline.loan_management.service.LoanApprovalHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanApprovalController.class)
class LoanApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @MockBean
    private LoanApprovalHistoryService historyService;

    @Autowired
    private LoanApprovalController loanApprovalController;

    @Test
    @WithMockUser(username="testuser")
    void testViewLoanDetails_Success() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        LoanApplication loan = new LoanApplication();
        loan.setId(1L);
        loan.setApplicantName("View Details User");

        when(loanApplicationService.getLoanById(1L)).thenReturn(loan);
        when(historyService.getHistoryForLoan(1L)).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(loanApprovalController, "securityEnabled", false);

        mockMvc.perform(get("/loanmanagement/loan/1").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loan.applicantName").value("View Details User"))
                .andExpect(jsonPath("$.history").isArray());
    }

    @Test
    @WithMockUser(username="testuser")
    void testUpdateLoanStatus_Success() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        LoanApplication loan = new LoanApplication();
        loan.setId(1L);

        when(loanApplicationService.getLoanById(1L)).thenReturn(loan);
        when(loanApplicationService.updateLoan(any(LoanApplication.class))).thenReturn(loan);
        when(historyService.addHistory(any(LoanApprovalHistory.class))).thenReturn(new LoanApprovalHistory());
        ReflectionTestUtils.setField(loanApprovalController, "securityEnabled", false);

        mockMvc.perform(post("/loanmanagement/loan/1/update")
                        .principal(mockPrincipal)
                        .param("step", "Verification")
                        .param("status", "Approved")
                        .param("comment", "All checks passed")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(loanApplicationService).updateLoan(any(LoanApplication.class));
        verify(historyService).addHistory(any(LoanApprovalHistory.class));
    }
}
