package com.example.expense_tracker_api.mapper;

import com.example.expense_tracker_api.model.ExpenseModel;
import com.example.expense_tracker_api.model.entity.ExpenseEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mappings({
            @Mapping(source = "employee.id", target = "employeeId"),
            @Mapping(source = "employee.name", target = "employeeName")
    })
    ExpenseModel toModel(ExpenseEntity entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mappings({
            @Mapping(source = "employeeId", target = "employee.id"),
            @Mapping(source = "employeeName", target = "employee.name")
    })
    ExpenseEntity toEntity(ExpenseModel model);

}
