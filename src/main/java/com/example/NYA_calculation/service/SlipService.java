package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.converter.SlipConverter;
import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.SlipRepository;
import com.example.NYA_calculation.repository.entity.Slip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlipService {

    @Autowired
    SlipRepository slipRepository;
    @Autowired
    SlipConverter slipConverter;

    public SlipForm createSlip(SlipForm slipForm) {
        slipRepository.insert(slipConverter.toEntity(slipForm));
        return slipForm;
    }

    public List<Slip> getAllSlips() {
        return slipRepository.findAll();
    }

    public SlipForm getSlip(Integer id) {
        return slipConverter.toForm(slipRepository.findById(id));
    }

    public List<SlipWithUserDto> getSTempSlips(Integer userId) {
        return slipRepository.findTemporaryByUserId(userId);
    }

    public Object getApprovalSlips(Integer approverId) {
        return slipRepository.findApprovalByApproverId(approverId);
    }

    public SlipForm updateSlips(SlipForm applySlip) {
        slipRepository.update(slipConverter.toEntity(applySlip));
        return applySlip;
    }
}
