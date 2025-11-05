package com.example.NYA_calculation.mapper;

import com.example.NYA_calculation.controller.form.UserForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userMapper {
    UserForm findById(Integer id);

}
