package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.NYA_calculation.error.RecordNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
    }

    public UserForm findFormById(Integer id) {
        return userRepository.findFormById(id);
    }

    public boolean updateUser(UserForm userForm) {
        if (userForm.getPassword() == null || userForm.getPassword().isBlank()) {
            userForm.setPassword(null);
        } else {
            userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }
        int updatedRows = userRepository.updateUser(userForm);
        return updatedRows > 0;
    }

    public List<User> getApprovers() {
        return userRepository.findApprovers();
    }

    public Page<UserForm> pageUser(int page, int size) {
        int offset = page * size;
        List<UserForm> users = userRepository.pageUser(size, offset);
        int totalCount = userRepository.countUsers(); // 全件数を別で取得

        return new PageImpl<>(users, PageRequest.of(page, size), totalCount);
    }

    public void changeIsStopped(Integer id, boolean isStopped) {
        userRepository.updateIsStopped(id, isStopped);
    }
}
