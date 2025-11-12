package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.repository.entity.Detail;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        detailForm.setFileName(result.getFileName());
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
            detailForm.setFileName(result.getFileName());
            detailForm.setSlipId(result.getSlipId());
            detailForm.setRemark(result.getRemark());
            detailForm.setCreatedDate(result.getCreatedDate());
            detailForm.setUpdatedDate(result.getUpdatedDate());
            detailForms.add(detailForm);
        }
        return detailForms;
    }

    public Detail toEntity(DetailForm form) throws IOException {

        String fileName = saveFile(form.getFile());

        Detail detail = new Detail();
        detail.setBillingDate(form.getBillingDate());
        detail.setRoundTrip(form.getRoundTrip());
        detail.setAmount(form.getAmount());
        detail.setSubtotal(form.getSubtotal());
        detail.setReason(form.getReason());
        detail.setTransportation(form.getTransportation());
        detail.setFileName(fileName);
        detail.setSlipId(form.getSlipId());
        detail.setRemark(form.getRemark());

        if (form.getId() != null) {
            detail.setId(form.getId());
            detail.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return detail;
    }

    private String saveFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) return null;

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("src/main/resources/static/receipt");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
        return fileName;
    }

}
