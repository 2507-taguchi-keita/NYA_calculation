package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.converter.UserConverter;
import com.example.NYA_calculation.dto.UserDto;
import com.example.NYA_calculation.error.RecordNotFoundException;
import com.example.NYA_calculation.repository.DepartmentRepository;
import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.Department;
import com.example.NYA_calculation.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserConverter userConverter;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
    }

    public UserForm findFormById(Integer id) {
        return userRepository.findFormById(id);
    }

    public boolean updateUser(UserForm userForm) {
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        } else {
            userForm.setPassword(null);
        }
        int updatedRows = userRepository.updateUser(userForm);
        return updatedRows >= 0;
    }

    public List<User> getApprovers(Integer departmentId) {
        return userRepository.findApprovers(departmentId);
    }

    public Page<UserForm> pageUser(int page, int size) {
        int offset = page * size;
        List<UserForm> users = userRepository.pageUser(size, offset);
        int totalCount = userRepository.countUsers();

        return new PageImpl<>(users, PageRequest.of(page, size), totalCount);
    }

    public void changeIsStopped(Integer id, boolean isStopped) {
        userRepository.updateIsStopped(id, isStopped);
    }

    // アカウント重複判定用
    public boolean isAccountExists(String account, Integer excludeUserId) {
        Integer count = userRepository.countByAccount(account, excludeUserId);
        return count != null && count > 0;
    }

    public boolean addUser(UserForm userForm) {

        //既に登録されている社員番号かを確認
        if (isAccountExists(userForm.getAccount(), null)) {
            return false;
        }

        User user = new User();
        user.setAccount(userForm.getAccount());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setName(userForm.getName());
        user.setAuthority(userForm.getAuthority());
        user.setDepartmentId(userForm.getDepartmentId());
        user.setStopped(false);
        user.setApproverId(userForm.getApproverId());
        userRepository.insertUser(user);
        return true;
    }

    @Transactional
    public boolean adminEditUser(UserForm userForm) {

        // パスワードが入力されていた場合のみエンコードして上書き
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        } else {
            userForm.setPassword(null);
        }

        try {
            int updatedRows = userRepository.adminEditUser(userForm);
            return updatedRows > 0;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public UserDto getUserDto(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException(E0013));
        Department department = departmentRepository.findById(user.getDepartmentId());
        User approver = userRepository.findById(user.getApproverId()).orElseThrow(() -> new RecordNotFoundException(E0013));

        return userConverter.toDto(user, department, approver);
    }

}
