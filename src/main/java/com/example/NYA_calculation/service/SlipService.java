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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    @Autowired
    FileStorageService fileStorageService;

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

    public List<SlipWithUserDto> getApplicationSlips(Integer userId) {
        return slipRepository.findApplicationByUserId(userId);
    }

    public List<SlipWithUserDto> getRemandSlips(Integer userId) {
        return slipRepository.findRemandByUserId(userId);
    }

    public List<SlipWithUserDto> getApprovalSlips(User loginUser) {
        List<SlipWithUserDto> results = slipRepository.findApprovalByStatus();


        if (loginUser.getDepartmentId() == 1 && loginUser.getAuthority() == 1) {
            return results.stream()
                    .filter(s -> s.getStep() == 1)
                    .toList();
        } else if (loginUser.getDepartmentId() == 1 && loginUser.getAuthority() == 2) {
            return results.stream()
                    .filter(s -> s.getStep() == 2)
                    .toList();
        } else {
            return results.stream()
                    .filter(s -> s.getStep() == 3 && Objects.equals(s.getApproverId(), loginUser.getId()))
                    .toList();
        }
    }

    public SlipForm getSlipForm(Integer slipId) {

        SlipForm slipForm = slipConverter.toForm(slipRepository.findById(slipId).orElseThrow(() -> new RecordNotFoundException(E0013)));
        List<Detail> details = detailRepository.findBySlipId(slipId);

        for (Detail d : details) {
            if (d.getStoredFileName() != null) {
                d.setFileUrl(fileStorageService.getPermanentFileUrl(d.getStoredFileName()));
            }
        }

        slipForm.setDetailForms(detailConverter.toFormList(details));

        return slipForm;
    }

    public void saveSlip(SlipForm slipForm) throws IOException {

        Slip slip = slipConverter.toEntity(slipForm);

        if (slip.getId() == null) {
            slipRepository.insertSlip(slip);
            slipForm.setId(slip.getId());
        } else {
            slipRepository.updateSlip(slip);
            detailRepository.deleteBySlipId(slip.getId());
        }

        List<Detail> details = slipConverter.toDetailEntities(slipForm.getDetailForms(), slip.getId());

        if (!details.isEmpty()) {
            detailRepository.insertDetails(details);
        }
    }

    public void deleteSlip(SlipForm slipForm) {
        slipRepository.deleteSlip(slipForm.getId());
        detailRepository.deleteBySlipId(slipForm.getId());
    }

}
