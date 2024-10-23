package com.example.expense_tracker_api.service;

import com.example.expense_tracker_api.error.ResourceNotFoundException;
import com.example.expense_tracker_api.mapper.ExpenseMapper;
import com.example.expense_tracker_api.model.ExpenseModel;
import com.example.expense_tracker_api.model.ExpenseStatus;
import com.example.expense_tracker_api.model.ExpenseSummary;
import com.example.expense_tracker_api.model.entity.ExpenseEntity;
import com.example.expense_tracker_api.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.expense_tracker_api.error.ErrorCode.RESOURCE_NOT_FOUND_DEFAULT_MESSAGE;

@RequiredArgsConstructor
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    /**
     * Retrieves a page of expenses by expense status.
     *
     * @param status   the status of the expenses are going to be retrieved.
     * @param pageable pagination details - page number and page size.
     * @return a page of {@link ExpenseModel} objects.
     */
    public Page<ExpenseModel> getExpensesByStatus(ExpenseStatus status,
                                                  Pageable pageable) {
        Page<ExpenseEntity> expenseEntities = status == null ?
                expenseRepository.findAll(pageable) :
                expenseRepository.findByStatus(status, pageable);
        return expenseEntities.map(ExpenseMapper.INSTANCE::toModel);
    }

    /**
     * Retrieves an expense record by ID.
     *
     * @param uuid the uuid of the expense that is going to be retrieved.
     * @return the target expense {@link ExpenseModel} object.
     */
    public ExpenseModel getExpense(UUID uuid) {
        ExpenseEntity expenseEntity = expenseRepository.findById(uuid)
                .orElseThrow(() -> ResourceNotFoundException.forError(
                        RESOURCE_NOT_FOUND_DEFAULT_MESSAGE, "Expense", uuid));
        return ExpenseMapper.INSTANCE.toModel(expenseEntity);
    }

    /**
     * Updates an expense record by ID.
     *
     * @param uuid   the uuid of the expense that is going to be retrieved.
     * @param status new status of the expense.
     * @return the updated expense {@link ExpenseModel} object.
     */
    public ExpenseModel updateExpenseStatus(UUID uuid, ExpenseStatus status) {
        ExpenseEntity expenseEntity = expenseRepository.findById(uuid)
                .orElseThrow(() -> ResourceNotFoundException.forError(
                        RESOURCE_NOT_FOUND_DEFAULT_MESSAGE, "Expense", uuid));
        expenseEntity.setStatus(status);
        return ExpenseMapper.INSTANCE.toModel(expenseRepository.save(expenseEntity));
    }

    /**
     * Get summary.
     *
     * @return the summary {@link ExpenseSummary} object.
     */
    public ExpenseSummary getSummary() {
        List<ExpenseEntity> expenseEntity = expenseRepository.findAll();

        int totalExpenses = expenseEntity.size();
        Map<String, Integer> categories = new ConcurrentHashMap<>();
        Map<String, Integer> statuses = new ConcurrentHashMap<>();
        AtomicReference<BigDecimal> amountAccumulator = new AtomicReference<>(BigDecimal.ZERO);

        expenseEntity.parallelStream().forEach(entity -> {
            amountAccumulator.updateAndGet(amount -> amount.add(entity.getAmount()));
            categories.merge(entity.getCategory(), 1, Integer::sum);
            statuses.merge(entity.getStatus().getDisplayName(), 1, Integer::sum);
        });

        BigDecimal totalAmount = amountAccumulator.get();

        return ExpenseSummary.builder()
                .totalExpenses(totalExpenses)
                .totalAmount(totalAmount)
                .categories(categories)
                .statuses(statuses)
                .build();
    }
}
