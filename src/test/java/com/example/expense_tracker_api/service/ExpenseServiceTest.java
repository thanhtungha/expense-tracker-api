package com.example.expense_tracker_api.service;

import com.example.expense_tracker_api.error.ResourceNotFoundException;
import com.example.expense_tracker_api.model.ExpenseModel;
import com.example.expense_tracker_api.model.ExpenseStatus;
import com.example.expense_tracker_api.model.ExpenseSummary;
import com.example.expense_tracker_api.model.entity.ExpenseEntity;
import com.example.expense_tracker_api.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock
    ExpenseRepository expenseRepository;

    @InjectMocks
    ExpenseService expenseService;

    @Test
    void getExpensesByStatus() {
        ExpenseStatus status = ExpenseStatus.PENDING;
        Pageable pageable = PageRequest.of(0, 10);
        ExpenseEntity expenseEntity1 = new ExpenseEntity();
        ExpenseEntity expenseEntity2 = new ExpenseEntity();
        Page<ExpenseEntity> expensePage = new PageImpl<>(
                Arrays.asList(expenseEntity1, expenseEntity2));
        when(expenseRepository.findByStatus(status, pageable)).thenReturn(expensePage);

        Page<ExpenseModel> result = expenseService.getExpensesByStatus(status, pageable);
        assertEquals(2, result.getTotalElements());
        verify(expenseRepository, times(1)).findByStatus(status, pageable);
    }

    @Test
    void getExpense_shouldReturnExpenseModel() {
        UUID expenseId = UUID.randomUUID();
        ExpenseEntity expenseEntity = new ExpenseEntity();
        when(expenseRepository.findById(expenseId)).thenReturn(
                Optional.of(expenseEntity));
        ExpenseModel result = expenseService.getExpense(expenseId);
        assertNotNull(result);
        verify(expenseRepository, times(1)).findById(expenseId);
    }

    @Test
    void getExpense_shouldThrowResourceNotFoundException() {
        UUID expenseId = UUID.randomUUID();
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class, () -> expenseService.getExpense(expenseId));
        verify(expenseRepository, times(1)).findById(expenseId);
    }

    @Test
    void updateExpenseStatus_shouldReturnUpdatedExpenseModel() {
        UUID expenseId = UUID.randomUUID();
        ExpenseStatus newStatus = ExpenseStatus.APPROVED;
        ExpenseEntity expenseEntity = new ExpenseEntity();
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expenseEntity));
        when(expenseRepository.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);
        ExpenseModel result = expenseService.updateExpenseStatus(expenseId, newStatus);
        assertNotNull(result);
        verify(expenseRepository, times(1)).findById(expenseId);
        verify(expenseRepository, times(1)).save(any(ExpenseEntity.class));
    }

    @Test
    void getSummary_shouldReturnExpenseSummary() {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setAmount(BigDecimal.valueOf(100));
        expenseEntity.setCategory("Travel");
        expenseEntity.setStatus(ExpenseStatus.PENDING);
        when(expenseRepository.findAll()).thenReturn(Collections.singletonList(expenseEntity));
        ExpenseSummary summary = expenseService.getSummary();
        assertEquals(1, summary.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(100), summary.getTotalAmount());
        assertEquals(1, summary.getCategories().get("Travel").intValue());
        verify(expenseRepository, times(1)).findAll();
    }

}