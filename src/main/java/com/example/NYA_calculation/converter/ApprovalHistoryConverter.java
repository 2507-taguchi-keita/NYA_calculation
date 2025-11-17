package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.dto.ApprovalHistoryDto;
import com.example.NYA_calculation.error.RecordNotFoundException;
import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.ApprovalHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Component
public class ApprovalHistoryConverter {

    @Autowired
    UserRepository userRepository;

    public List<ApprovalHistoryDto> toDto(List<ApprovalHistory> approvalHistories) {

        List<ApprovalHistoryDto> approvalHistoryDtoList = new ArrayList<>();

        for (ApprovalHistory approvalHistory : approvalHistories) {
            ApprovalHistoryDto approvalHistoryDto = new ApprovalHistoryDto();
            approvalHistoryDto.setApprovalHistory(approvalHistory);
            approvalHistoryDto.setUser(userRepository.findById(approvalHistory.getUserId())
                    .orElseThrow(() -> new RecordNotFoundException(E0013)));
            approvalHistoryDtoList.add(approvalHistoryDto);
        }

        return approvalHistoryDtoList;
    }

}
