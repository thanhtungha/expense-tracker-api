package com.example.expense_tracker_api.annotation;

import com.example.expense_tracker_api.model.ExpenseStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class AllowedExpenseStatusValidator implements ConstraintValidator<AllowedExpenseStatus, ExpenseStatus> {

    private ExpenseStatus[] allowedValues;

    @Override
    public void initialize(AllowedExpenseStatus constraintAnnotation) {
        this.allowedValues = constraintAnnotation.allowedValues();
    }

    @Override
    public boolean isValid(ExpenseStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        for (ExpenseStatus allowedValue : allowedValues) {
            if (allowedValue == value) {
                return true;
            }
        }

        return false;
    }
}
