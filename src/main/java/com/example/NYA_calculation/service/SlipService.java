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
        Slip slip = slipConverter.toEntity(slipForm);
        slipRepository.insert(slip);
        slipForm.setId(slip.getId());
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
        Slip slip = slipConverter.toEntity(applySlip);
        slipRepository.update(slip);
        applySlip.setId(slip.getId());
        return applySlip;
    }
    public List<Slip> findByUserId(Integer id) {
        return slipRepository.findByUserId(id);
    }

    public boolean cancelSlip(Integer slipId, Integer userId) {
        Slip slip = slipRepository.findById(slipId);
        if (slip == null || !slip.getUserId().equals(userId)) {
            return false;
        }
        return slipRepository.updateStatus(slipId, 0) > 0; // 一時保存に戻す
    }

    // IDで伝票を1件取得
    public Slip findById(Integer slipId) {
        return slipRepository.findById(slipId);
    }

    public List<Slip> findByUserIdSlips(Integer id) {
        return slipRepository.findByUserIdSlips(id);
    }
}
