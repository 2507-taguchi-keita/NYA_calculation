package com.example.NYA_calculation.dto;

import com.example.NYA_calculation.repository.entity.Department;
import com.example.NYA_calculation.repository.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private User user;

    private Department department;

    private User approver;

}
