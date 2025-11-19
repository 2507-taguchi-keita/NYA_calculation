package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.controller.form.UserForm;
import com.example.NYA_calculation.dto.UserDto;
import com.example.NYA_calculation.repository.entity.Department;
import com.example.NYA_calculation.repository.entity.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    public UserForm toForm(User result) {
        UserForm userForm = new UserForm();

        userForm.setId(result.getId());
        userForm.setAccount(result.getAccount());
        userForm.setPassword(result.getPassword());
        userForm.setName(result.getName());
        userForm.setDepartmentId(result.getDepartmentId());
        userForm.setAuthority(result.getAuthority());
        userForm.setStopped(result.isStopped());
        userForm.setApproverId(result.getApproverId());
        userForm.setCreatedDate(result.getCreatedDate());
        userForm.setUpdatedDate(result.getUpdatedDate());
        return userForm;
    }

    public List<UserForm> toFormList(List<User> results) {
        List<UserForm> userForms = new ArrayList<>();

        for (User result : results) {
            UserForm userForm = new UserForm();
            userForm.setId(result.getId());
            userForm.setAccount(result.getAccount());
            userForm.setPassword(result.getPassword());
            userForm.setName(result.getName());
            userForm.setDepartmentId(result.getDepartmentId());
            userForm.setAuthority(result.getAuthority());
            userForm.setStopped(result.isStopped());
            userForm.setApproverId(result.getApproverId());
            userForm.setCreatedDate(result.getCreatedDate());
            userForm.setUpdatedDate(result.getUpdatedDate());
            userForms.add(userForm);
        }
        return userForms;
    }

    public User toEntity(UserForm form) {

        User user = new User();
        user.setAccount(form.getAccount());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setDepartmentId(form.getDepartmentId());
        user.setAuthority(form.getAuthority());
        user.setStopped(form.isStopped());
        user.setApproverId(form.getApproverId());

        if (form.getId() != null) {
            user.setId(form.getId());
            user.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return user;
    }

    public UserDto toDto(User user, Department department, User approver) {

        UserDto userDto = new UserDto();
        userDto.setUser(user);
        userDto.setDepartment(department);
        userDto.setApprover(approver);

        return userDto;
    }

}
