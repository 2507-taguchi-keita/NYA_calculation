package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.converter.DetailConverter;
import com.example.NYA_calculation.converter.SlipConverter;
import com.example.NYA_calculation.dto.SlipWithUserDto;
import com.example.NYA_calculation.error.RecordNotFoundException;
import com.example.NYA_calculation.repository.*;
import com.example.NYA_calculation.repository.entity.Detail;
import com.example.NYA_calculation.repository.entity.Slip;
import com.example.NYA_calculation.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Service
public class SlipService {

    @Autowired
    SlipRepository slipRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    ApprovalHistoryRepository approvalHistoryRepository;
    @Autowired
    DetailRepository detailRepository;

    @Autowired
    SlipConverter slipConverter;
    @Autowired
    DetailConverter detailConverter;

    public List<Slip> getAllSlips() {
        return slipRepository.findAll();
    }

    public SlipForm getSlip(Integer id) {
        return slipConverter.toForm(slipRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0013)));
    }

    public List<SlipWithUserDto> getTemporarySlips(Integer userId) {
        return slipRepository.findTemporaryByUserId(userId);
    }

    public List<SlipWithUserDto> getApprovalSlips(User loginUser) {
        List<SlipWithUserDto> results = slipRepository.findApprovalByStatus();


        if (loginUser.getDepartmentId() == 1 && loginUser.getAuthority() == 1) {
            return  results.stream()
                    .filter(s -> s.getStep() == 1)
                    .map(s -> new SlipWithUserDto())
                    .toList();
        } else if (loginUser.getDepartmentId() == 1 && loginUser.getAuthority() == 2) {
            return results.stream()
                    .filter(s -> s.getStep() == 2)
                    .map(s -> new SlipWithUserDto())
                    .toList();
        } else {
            return results.stream()
                    .filter(s -> s.getStep() == 3 && Objects.equals(s.getApproverId(), loginUser.getId()) )
                    .map(s -> new SlipWithUserDto())
                    .toList();
        }
    }

    public List<Slip> findByUserId(Integer id) {
        return slipRepository.findByUserId(id);
    }

    public boolean cancelSlip(Integer slipId, Integer userId) {
        Slip slip = slipRepository.findById(slipId)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
        if (slip == null || !slip.getUserId().equals(userId)) {
            return false;
        }
        return slipRepository.updateStatus(slipId, 0) > 0; // 一時保存に戻す
    }

    // IDで伝票を1件取得
    public Slip findById(Integer slipId) {
        return slipRepository.findById(slipId)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
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

    public List<Slip> findByUserIdSlips(Integer id) {
        return slipRepository.findByUserIdSlips(id);
    }

    public boolean reapplicationSlip(Integer slipId, Integer id) {
        Slip slip = slipRepository.findById(slipId)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
        if (slip == null || !slip.getUserId().equals(id)) {
            return false;
        }
        return slipRepository.updateStatus(slipId, 1) > 0;
    }

    public SlipForm getSlipForm(Integer slipId) {

        SlipForm slipForm = slipConverter.toForm(slipRepository.findById(slipId).orElseThrow(() -> new RecordNotFoundException(E0013)));
        slipForm.setDetailForms(detailConverter.toFormList(detailRepository.findBySlipId(slipId)));

        return slipForm;
    }

}
