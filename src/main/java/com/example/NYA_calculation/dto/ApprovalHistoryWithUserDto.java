package com.example.NYA_calculation.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApprovalHistoryWithUserDto {
    private Integer id;
    private Integer slipId;
    private Integer userId;
    private String userName;
    private Integer role;
    private Timestamp approvedAt;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
