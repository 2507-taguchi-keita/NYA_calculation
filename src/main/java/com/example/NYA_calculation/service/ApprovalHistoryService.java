package com.example.NYA_calculation.service;

import com.example.NYA_calculation.dto.ApprovalHistoryWithUserDto;
import com.example.NYA_calculation.repository.ApprovalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalHistoryService {

    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;

    public void saveApprovalHistory(Integer slipId, Integer userId, Integer role) {
        approvalHistoryRepository.insert(slipId, userId, role);
    }

    public List<ApprovalHistoryWithUserDto> getHistory(Integer slipId) {
        return approvalHistoryRepository.findBySlipId(slipId);
    }
}
