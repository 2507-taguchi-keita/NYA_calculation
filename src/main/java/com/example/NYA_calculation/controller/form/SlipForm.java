package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SlipForm {

    private Integer id;

    private Integer status;

    private Integer step;

    private Integer totalAmount;

    private Integer userId;

    private Integer approverId;

    private LocalDateTime applicationDate;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private List<DetailForm> detailForms = new ArrayList<>();

    private List<DetailForm> csvDetailForms = new ArrayList<>();

}
