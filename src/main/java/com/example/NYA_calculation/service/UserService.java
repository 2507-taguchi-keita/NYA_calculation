package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    userMapper userMapper;

    public UserForm findById(Integer id) {
        return userMapper.findById(id);
    }
}
