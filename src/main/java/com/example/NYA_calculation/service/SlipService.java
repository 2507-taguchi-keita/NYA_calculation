package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.converter.SlipConverter;
import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.repository.DetailRepository;
import com.example.NYA_calculation.repository.SlipRepository;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.Slip;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlipService {

    @Autowired
    SlipRepository slipRepository;
    @Autowired
    SlipConverter slipConverter;
    @Autowired
    DetailRepository detailRepository;

    public List<Slip> getAllSlips() {
        return slipRepository.findAll();
    }

    public SlipForm getSlip(Integer id) {
        return slipConverter.toForm(slipRepository.findById(id));
    }

    public List<SlipWithUserDto> getSTemporarySlips(Integer userId) {
        return slipRepository.findTemporaryByUserId(userId);
    }

    public List<SlipWithUserDto> getApprovalSlips(Integer approverId) {
        return slipRepository.findApprovalByApproverId(approverId);
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

    @Transactional
    public void saveSlip(SlipForm slipForm) {

        Slip slip = slipConverter.toEntity(slipForm);

        if (slip.getId() == null) {
            slipRepository.insertSlip(slip);
            slipForm.setId(slip.getId());
        } else {
            slipRepository.updateSlip(slip);
            detailRepository.deleteBySlipId(slip.getId());
        }

        // 明細を SlipDetail に変換
        List<Detail> details = slipConverter.toDetailEntities(slipForm.getDetailForms(), slip.getId(), slip.getUserId());

        if (!details.isEmpty()) {
            detailRepository.insertDetails(details);
        }
    }

    @Transactional
    public void deleteSlip(SlipForm slipForm) {
        slipRepository.deleteSlip(slipForm.getId());
        detailRepository.deleteBySlipId(slipForm.getId());
    }
}
