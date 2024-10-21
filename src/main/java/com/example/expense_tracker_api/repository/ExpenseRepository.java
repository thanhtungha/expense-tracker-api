package com.example.expense_tracker_api.repository;

import com.example.expense_tracker_api.model.ExpenseStatus;
import com.example.expense_tracker_api.model.entity.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {
    Page<ExpenseEntity> findByStatus(ExpenseStatus status, Pageable pageable);
}