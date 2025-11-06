package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserForm findById(Integer id) {
        return userRepository.findById(id);
    }

    public boolean updateUser(UserForm userForm) {

        // パスワードが入力されている場合のみ暗号化
        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
            String encoded = passwordEncoder.encode(userForm.getPassword());
            userForm.setPassword(encoded);
        }

        int updatedRows = userRepository.updateUser(userForm);
        return updatedRows > 0;
    }

    public List<User> getApprovers() {
        return userRepository.findApprovers();
    }
}
