package com.example.NYA_calculation.service;

import com.example.NYA_calculation.repository.SlipRepository;
import com.example.NYA_calculation.repository.entity.Slip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlipService {

    @Autowired
    SlipRepository slipRepository;

    public Integer createSlip(Slip slip) {
        slipRepository.insert(slip);
        return slip.getId();
    }

    public List<Slip> getAllSlips() {
        return slipRepository.findAll();
    }
    public List<Slip> findByUserId(Integer id) {
        return slipRepository.findByUserId(id);
    }

}
