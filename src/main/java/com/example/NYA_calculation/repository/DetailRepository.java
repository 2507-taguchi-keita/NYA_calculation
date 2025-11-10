package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.entity.Detail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailRepository {
    List<ExpenseSummary> getMonthlyTotal();

    /**
     * 明細追加
     */
    void insert(Detail detail);

    List<Detail> findBySlipId(Integer slipId);
}
