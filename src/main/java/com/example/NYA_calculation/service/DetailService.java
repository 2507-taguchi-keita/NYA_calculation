package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.converter.DetailConverter;
import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.DetailRepository;
import com.example.NYA_calculation.repository.entity.Detail;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DetailService {

    @Autowired
    DetailRepository detailRepository;
    @Autowired
    DetailConverter detailConverter;

    public List<ExpenseSummary> getMonthlyTotal() {
        return detailRepository.getMonthlyTotal();
    }

    public void save(DetailForm detailForm) throws IOException {
        detailRepository.insert(detailConverter.toEntity(detailForm));
    }

    public void insert(Detail detail) throws IOException {
        detailRepository.insert(detail);
    }

    public List<DetailForm> getDetails(Integer sLipId) {
        return detailConverter.toFormList(detailRepository.findBySlipId(sLipId));
    }

    public List<Detail> findBySlipId(Integer slipId) {
        return detailRepository.findBySlipId(slipId);
    }

    @Transactional
    public void updateDetail(DetailForm form) throws IOException {
        Detail detail = detailConverter.toEntity(form);
        detail.setId(form.getId());
        detail.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

        // 差し戻し編集専用ロジック（必要に応じて拡張可能）
        detailRepository.update(detail);
    }
}
