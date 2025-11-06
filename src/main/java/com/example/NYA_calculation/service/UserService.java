package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public UserForm findById(Integer id) {
        return userMapper.findById(id);
    }

    public boolean updateUser(UserForm userForm) {
        int updatedRows = userMapper.updateUser(userForm);
        return updatedRows > 0;
    }
}
