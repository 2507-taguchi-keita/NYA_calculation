package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.controller.form.SlipForm;
import com.example.NYA_calculation.repository.entity.Slip;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlipConverter {

    public SlipForm toForm(Slip result) {
        SlipForm slipForm = new SlipForm();

        slipForm.setId(result.getId());
        slipForm.setStatus(result.getStatus());
        slipForm.setStep(result.getStep());
        slipForm.setTotalAmount(result.getTotalAmount());
        slipForm.setUserId(result.getUserId());
        slipForm.setApproverId(result.getApproverId());
        slipForm.setCreatedDate(result.getCreatedDate());
        slipForm.setUpdatedDate(result.getUpdatedDate());
        return slipForm;
    }

    public List<SlipForm> toFormList(List<Slip> results) {
        List<SlipForm> slipForms = new ArrayList<>();

        for (Slip result : results) {
            SlipForm slipForm = new SlipForm();
            slipForm.setId(result.getId());
            slipForm.setStatus(result.getStatus());
            slipForm.setStep(result.getStep());
            slipForm.setTotalAmount(result.getTotalAmount());
            slipForm.setUserId(result.getUserId());
            slipForm.setApproverId(result.getApproverId());
            slipForm.setCreatedDate(result.getCreatedDate());
            slipForm.setUpdatedDate(result.getUpdatedDate());
            slipForms.add(slipForm);
        }
        return slipForms;
    }

    public Slip toEntity(SlipForm form) {

        Slip slip = new Slip();
        slip.setStatus(form.getStatus());
        slip.setStep(form.getStep());
        slip.setTotalAmount(form.getTotalAmount());
        slip.setUserId(form.getUserId());
        slip.setApproverId(form.getApproverId());

        if (form.getId() != null) {
            slip.setId(form.getId());
            slip.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return slip;
    }

}
