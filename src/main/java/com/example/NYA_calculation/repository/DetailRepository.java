package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.dto.ExpenseSummary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailRepository {
    List<ExpenseSummary> getMonthlyTotal();
}
