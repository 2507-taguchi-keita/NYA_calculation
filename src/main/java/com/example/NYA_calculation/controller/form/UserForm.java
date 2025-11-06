package com.example.NYA_calculation.controller.form;

import com.example.NYA_calculation.validation.CreateGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static com.example.NYA_calculation.validation.ErrorMessage.*;

@Getter
@Setter
public class UserForm {
    private Integer id;

    @NotEmpty(message = E0001, groups = {CreateGroup.class})
    @Size(max = 7, message = E0018, groups = {CreateGroup.class})
    private String account;

    @NotEmpty(message = E0002, groups = {CreateGroup.class})
    @Pattern(regexp = "^(?:$|[\\x21-\\x7E]{6,20})$", message = E0005)
    private String password;
    private String confirmPassword;

    @NotEmpty(message = E0015, groups = {CreateGroup.class})
    @Size(max = 10, message = E0019, groups = {CreateGroup.class})
    private String name;

    private Integer departmentId;
    private Integer authority;
    private boolean isStopped;

    @NotNull(message = E0004)
    private Integer approverId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
