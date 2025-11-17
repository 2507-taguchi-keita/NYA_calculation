package com.example.NYA_calculation.service;

import com.example.NYA_calculation.converter.ApprovalHistoryConverter;
import com.example.NYA_calculation.dto.ApprovalHistoryDto;
import com.example.NYA_calculation.repository.ApprovalHistoryRepository;
import com.example.NYA_calculation.repository.entity.ApprovalHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalHistoryService {

    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    ApprovalHistoryConverter approvalHistoryConverter;

    public void saveApprovalHistory(Integer slipId, Integer userId) {
        approvalHistoryRepository.insert(slipId, userId);
    }

    public List<ApprovalHistoryDto> getApprovalHistoryDtoList(Integer slipId) {
        List<ApprovalHistory> approvalHistories =  approvalHistoryRepository.findBySlipId(slipId);
        return approvalHistoryConverter.toDto(approvalHistories);
    }

}
