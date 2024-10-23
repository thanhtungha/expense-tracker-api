package com.example.expense_tracker_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSummary {
    private int totalExpenses;
    private BigDecimal totalAmount;
    private Map<String, Integer> categories;
    private Map<String, Integer> statuses;
}
