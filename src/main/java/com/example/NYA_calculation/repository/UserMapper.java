package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.controller.form.UserForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserForm findById(Integer id);
    int updateUser(UserForm userForm);
}
