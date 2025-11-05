package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SlipForm {
    private Integer id;
    private Integer status;
    private Integer step;
    private Integer totalAmount;
    private Integer userId;
    private Integer approverId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
