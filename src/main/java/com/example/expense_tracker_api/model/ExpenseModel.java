package com.example.expense_tracker_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseModel {
    private UUID id;
    private Instant createdAt;
    private String createdBy;
    private Instant lastUpdatedAt;
    private String lastUpdatedBy;
    private UUID employeeId;
    private String employeeName;
    private Date date;
    private String category;
    private BigDecimal amount;
    private String description;
    private ExpenseStatus status;
}
