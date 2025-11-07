package com.example.NYA_calculation.service;

import com.example.NYA_calculation.repository.DepartmentRepository;
import com.example.NYA_calculation.repository.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Department> getDepartments() {
        return departmentRepository.getDepartments();
    }
}
