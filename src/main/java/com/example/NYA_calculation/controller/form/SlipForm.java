package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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
    private Timestamp applicationDate;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private List<DetailForm> detailForms;

    public SlipForm() {
        this.detailForms = new ArrayList<>();
    }

}
