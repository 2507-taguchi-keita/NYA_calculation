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

    public Integer createSlip(Slip slip) {
        slipRepository.insert(slip);
        return slip.getId();
    }

    public List<Slip> getAllSlips() {
        return slipRepository.findAll();
    }

    public SlipForm getSlip(Integer id) {
        return slipConverter.toForm(slipRepository.findById(id));
    }

    public List<SlipWithUserDto> getSlips(Integer userId) {
        return slipRepository.findByUserId(userId);
    }
}
