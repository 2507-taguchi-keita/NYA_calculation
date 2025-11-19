package com.example.NYA_calculation.dto;

import com.example.NYA_calculation.repository.entity.ApprovalHistory;
import com.example.NYA_calculation.repository.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalHistoryDto {

    private ApprovalHistory approvalHistory;

    private User user;

}
