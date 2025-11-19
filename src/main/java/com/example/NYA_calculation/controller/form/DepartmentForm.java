package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DepartmentForm {

    private Integer id;

    private String name;

    private Timestamp createdDate;

    private Timestamp updatedDate;

}
