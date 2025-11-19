package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.repository.entity.Detail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetailConverter {

    public DetailForm toForm(Detail result) {
        DetailForm detailForm = new DetailForm();

        detailForm.setId(result.getId());
        detailForm.setBillingDate(result.getBillingDate());
        detailForm.setRoundTrip(result.getRoundTrip());
        detailForm.setAmount(result.getAmount());
        detailForm.setSubtotal(result.getSubtotal());
        detailForm.setReason(result.getReason());
        detailForm.setTransportation(result.getTransportation());
        //detailForm.(result.getFileName());
        detailForm.setSlipId(result.getSlipId());
        detailForm.setRemark(result.getRemark());
        detailForm.setCreatedDate(result.getCreatedDate());
        detailForm.setUpdatedDate(result.getUpdatedDate());
        return detailForm;
    }

    public List<DetailForm> toFormList(List<Detail> results) {
        List<DetailForm> detailForms = new ArrayList<>();

        for (Detail result : results) {
            DetailForm detailForm = new DetailForm();
            detailForm.setId(result.getId());
            detailForm.setBillingDate(result.getBillingDate());
            detailForm.setRoundTrip(result.getRoundTrip());
            detailForm.setAmount(result.getAmount());
            detailForm.setSubtotal(result.getSubtotal());
            detailForm.setReason(result.getReason());
            detailForm.setTransportation(result.getTransportation());
            detailForm.setOriginalFileName(result.getOriginalFileName());
            detailForm.setStoredFileName(result.getStoredFileName());
            detailForm.setFileUrl(result.getFileUrl());
            detailForm.setSlipId(result.getSlipId());
            detailForm.setRemark(result.getRemark());
            detailForm.setCreatedDate(result.getCreatedDate());
            detailForm.setUpdatedDate(result.getUpdatedDate());
            detailForms.add(detailForm);
        }
        return detailForms;
    }

}
