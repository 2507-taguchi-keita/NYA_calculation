package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.entity.Slip;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SlipRepository {
    List<ExpenseSummary> getMonthlyTotal();

    void insert(Slip slip);
    List<Slip> findAll();
    List<Slip> findByUserId(Integer id);

    Slip findById(Integer id);

    List<SlipWithUserDto> findTempByUserId(Integer userId);

    List<SlipWithUserDto> findApprovalByApproverId(Integer approverId);
}
