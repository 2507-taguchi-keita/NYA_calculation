package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.entity.Detail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface DetailRepository {

    void insertDetails(@Param("list") List<Detail> details);
    void deleteBySlipId(Integer slipId);

    List<ExpenseSummary> getMonthlyTotal();

    List<Detail> findBySlipId(@Param("slipId") Integer slipId);

}
