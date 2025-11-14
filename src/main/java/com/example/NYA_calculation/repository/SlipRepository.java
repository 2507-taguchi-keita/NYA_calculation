package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.entity.Slip;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SlipRepository {

    void insertSlip(Slip slip);
    void updateSlip(Slip slip);
    void deleteSlip(Integer id);

    List<Slip> findAll();

    List<Slip> findByUserId(Integer id);

    Optional<Slip> findById(Integer id);

    List<SlipWithUserDto> findTemporaryByUserId(Integer userId);

    List<SlipWithUserDto> findApprovalByApproverId(Integer approverId);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    List<Slip> findByUserIdSlips(Integer id);
}
