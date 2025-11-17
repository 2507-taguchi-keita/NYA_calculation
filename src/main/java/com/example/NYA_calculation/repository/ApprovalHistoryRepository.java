package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.repository.entity.ApprovalHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalHistoryRepository {

    void insert(Integer slipId, Integer userId);

    List<ApprovalHistory> findBySlipId(Integer slipId);
}
