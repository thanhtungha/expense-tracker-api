package com.example.expense_tracker_api.controller;

import com.example.expense_tracker_api.annotation.AllowedExpenseStatus;
import com.example.expense_tracker_api.model.ExpenseModel;
import com.example.expense_tracker_api.model.ExpenseStatus;
import com.example.expense_tracker_api.model.ExpenseSummary;
import com.example.expense_tracker_api.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "expenses-api", description = "Expense operations")
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ExpenseModel>> getAllExpenses(
            @RequestParam(value = "status", required = false)
            @Valid ExpenseStatus status,
            @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Getting expenses search status [{}]", status);
        return ResponseEntity.ok(expenseService.getExpensesByStatus(status, pageable));
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseModel> getExpenseById(
            @PathVariable @Valid UUID id) {
        log.info("Getting expense with id [{}]", id);
        return ResponseEntity.ok(expenseService.getExpense(id));
    }

    @PutMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseModel> updateExpenseStatus(
            @PathVariable @Valid UUID id,
            @AllowedExpenseStatus(allowedValues = {ExpenseStatus.APPROVED, ExpenseStatus.REJECTED})
            @RequestParam("status") ExpenseStatus status) {
        log.info("[{}] expense with id [{}]", status, id);
        return ResponseEntity.ok(expenseService.updateExpenseStatus(id, status));
    }

    @GetMapping(value = "/summary", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseSummary> getSummary() {
        log.info("Getting expenses summary");
        return ResponseEntity.ok(expenseService.getSummary());
    }
}
