package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.repository.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentRepository {

    Department findById(Integer departmentId);

}
