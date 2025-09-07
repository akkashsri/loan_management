package com.twinline.loan_management.controllerTest;

import com.twinline.loan_management.controller.DashboardController;
import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.service.LoanApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Autowired
    private DashboardController dashboardController;

    @Test
    @WithMockUser(username="testuser")
    void testGetDashboard_Success() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        LoanApplication loan = new LoanApplication();
        loan.setId(1L);
        loan.setApplicantName("Test User");

        when(loanApplicationService.getAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/loanmanagement/dashboard").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicantName").value("Test User"));
    }
}
