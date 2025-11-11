package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.ApprovalHistoryWithUserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalHistoryRepository {

    void insert(Integer slipId, Integer userId, Integer role);

    List<ApprovalHistoryWithUserDto> findBySlipId(Integer slipId);
}
