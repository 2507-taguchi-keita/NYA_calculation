package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserForm {

    private Integer id;
    private String account;
    private String password;
    private String confirmPassword;
    private String name;
    private Integer departmentId;
    private Integer authority;
    private boolean isStopped;
    private Integer approverId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
