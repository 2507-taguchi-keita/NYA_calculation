package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.entity.Slip;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SlipRepository {
    List<ExpenseSummary> getMonthlyTotal();

    void insert(Slip slip);

    List<Slip> findAll();

    List<Slip> findByUserId(Integer id);

    Slip findById(Integer id);

    List<SlipWithUserDto> findTemporaryByUserId(Integer userId);

    List<SlipWithUserDto> findApprovalByApproverId(Integer approverId);

    void update(Slip slip);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    List<Slip> findByUserIdSlips(Integer id);
}
