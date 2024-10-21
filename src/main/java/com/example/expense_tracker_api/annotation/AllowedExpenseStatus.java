package com.example.expense_tracker_api.annotation;

import com.example.expense_tracker_api.model.ExpenseStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AllowedExpenseStatusValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedExpenseStatus {

    String message() default "Invalid status value";

    ExpenseStatus[] allowedValues();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}