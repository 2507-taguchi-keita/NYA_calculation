package com.example.NYA_calculation.converter;

import com.example.NYA_calculation.controller.form.DetailTempForm;
import com.example.NYA_calculation.repository.entity.DetailTemp;
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
public class DetailTempConverter {

    public DetailTempForm toForm(DetailTemp result) {
        DetailTempForm detailTempForm = new DetailTempForm();

        detailTempForm.setId(result.getId());
        detailTempForm.setBillingDate(result.getBillingDate());
        detailTempForm.setRoundTrip(result.getRoundTrip());
        detailTempForm.setAmount(result.getAmount());
        detailTempForm.setSubtotal(result.getSubtotal());
        detailTempForm.setReason(result.getReason());
        detailTempForm.setTransportation(result.getTransportation());
        detailTempForm.setFileName(result.getFileName());
        detailTempForm.setSlipTempKey(result.getSlipTempKey());
        detailTempForm.setRemark(result.getRemark());
        detailTempForm.setUserId(result.getUserId());
        detailTempForm.setCreatedDate(result.getCreatedDate());
        detailTempForm.setUpdatedDate(result.getUpdatedDate());
        return detailTempForm;
    }

    public List<DetailTempForm> toFormList(List<DetailTemp> results) {
        List<DetailTempForm> detailTempForms = new ArrayList<>();

        for (DetailTemp result : results) {
            DetailTempForm detailTempForm = new DetailTempForm();
            detailTempForm.setId(result.getId());
            detailTempForm.setBillingDate(result.getBillingDate());
            detailTempForm.setRoundTrip(result.getRoundTrip());
            detailTempForm.setAmount(result.getAmount());
            detailTempForm.setSubtotal(result.getSubtotal());
            detailTempForm.setReason(result.getReason());
            detailTempForm.setTransportation(result.getTransportation());
            detailTempForm.setFileName(result.getFileName());
            detailTempForm.setSlipTempKey(result.getSlipTempKey());
            detailTempForm.setRemark(result.getRemark());
            detailTempForm.setUserId(result.getUserId());
            detailTempForm.setCreatedDate(result.getCreatedDate());
            detailTempForm.setUpdatedDate(result.getUpdatedDate());
            detailTempForms.add(detailTempForm);
        }
        return detailTempForms;
    }

    public DetailTemp toEntity(DetailTempForm form) throws IOException {

        String fileName = saveFile(form.getFile());

        DetailTemp detailTemp = new DetailTemp();
        detailTemp.setBillingDate(form.getBillingDate());
        detailTemp.setRoundTrip(form.getRoundTrip());
        detailTemp.setAmount(form.getAmount());
        detailTemp.setSubtotal(form.getSubtotal());
        detailTemp.setReason(form.getReason());
        detailTemp.setTransportation(form.getTransportation());
        detailTemp.setFileName(fileName);
        detailTemp.setSlipTempKey(form.getSlipTempKey());
        detailTemp.setRemark(form.getRemark());
        detailTemp.setUserId(form.getUserId());

        if (form.getId() != null) {
            detailTemp.setId(form.getId());
            detailTemp.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        }
        return detailTemp;
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
