package com.twinline.loan_management.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinline.loan_management.controller.LoanApplicationController;
import com.twinline.loan_management.entities.LoanApplication;
import com.twinline.loan_management.service.LoanApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanApplicationController.class)
class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Autowired
    private LoanApplicationController loanApplicationController;

    private LoanApplication loan;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        loan = new LoanApplication();
        loan.setId(1L);
        loan.setApplicantName("Jane Doe");
        loan.setLoanAmount(5000.0);
        loan.setTenure(12);
        loan.setIncome(2000.0);
        loan.setContactDetails("jane.doe@email.com");
    }

    @Test
    @WithMockUser(username="testuser")
    void testSubmitLoan_Success() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(loanApplicationService.createLoan(any(LoanApplication.class))).thenReturn(loan);

        mockMvc.perform(post("/loanmanagement/application/new")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicantName").value("Jane Doe"));
    }

    @Test
    @WithMockUser(username="testuser")
    void testSubmitLoan_BadRequest() throws Exception {
        LoanApplication incompleteLoan = new LoanApplication();
        incompleteLoan.setApplicantName("Jane Doe");

        mockMvc.perform(post("/loanmanagement/application/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteLoan))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
