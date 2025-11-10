package com.example.NYA_calculation.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SlipWithUserDto {
    private Integer id;
    private Integer status;
    private Integer step;
    private Integer totalAmount;
    private Integer userId;
    private String userName;
    private Integer approverId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
