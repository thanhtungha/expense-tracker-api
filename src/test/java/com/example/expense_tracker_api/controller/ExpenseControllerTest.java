package com.example.expense_tracker_api.controller;

import com.example.expense_tracker_api.model.ExpenseModel;
import com.example.expense_tracker_api.model.ExpenseStatus;
import com.example.expense_tracker_api.model.ExpenseSummary;
import com.example.expense_tracker_api.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ExpenseController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    private ExpenseModel expenseModel;
    private UUID expenseId;

    @BeforeEach
    void setup() {
        expenseId = UUID.randomUUID();
        expenseModel = new ExpenseModel();
        expenseModel.setId(expenseId);
        expenseModel.setAmount(BigDecimal.valueOf(100.00));
        expenseModel.setStatus(ExpenseStatus.PENDING);
        expenseModel.setCategory("Travel");
    }

    @Test
    void testGetAllExpenses() throws Exception {
        Page<ExpenseModel> expenses = new PageImpl<>(Arrays.asList(expenseModel), PageRequest.of(0, 10), 1);

        when(expenseService.getExpensesByStatus(any(ExpenseStatus.class), any(Pageable.class))).thenReturn(expenses);

        mockMvc.perform(get("/api/expenses")
                        .param("status", "PENDING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(expenseId.toString()))
                .andExpect(jsonPath("$.content[0].amount").value(100.00))
                .andExpect(jsonPath("$.content[0].category").value("Travel"));
    }

    @Test
    void testGetExpenseById() throws Exception {
        when(expenseService.getExpense(eq(expenseId)))
                .thenReturn(expenseModel);

        mockMvc.perform(get("/api/expenses/{id}", expenseId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expenseId.toString()))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.category").value("Travel"));
    }

    @Test
    void testUpdateExpenseStatus() throws Exception {
        ExpenseModel updatedExpense = new ExpenseModel();
        updatedExpense.setId(expenseId);
        updatedExpense.setStatus(ExpenseStatus.APPROVED);

        when(expenseService.updateExpenseStatus(eq(expenseId), eq(ExpenseStatus.APPROVED)))
                .thenReturn(updatedExpense);

        mockMvc.perform(put("/api/expenses/{id}", expenseId.toString())
                        .param("status", "APPROVED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testGetSummary() throws Exception {
        ExpenseSummary expenseSummary = new ExpenseSummary();
        expenseSummary.setTotalExpenses(10);
        expenseSummary.setTotalAmount(BigDecimal.valueOf(1000.00));

        when(expenseService.getSummary()).thenReturn(expenseSummary);

        mockMvc.perform(get("/api/expenses/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalExpenses").value(10))
                .andExpect(jsonPath("$.totalAmount").value(1000.00));
    }

}