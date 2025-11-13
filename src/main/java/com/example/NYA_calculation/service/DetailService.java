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
        detailRepository.update(detail);
    }

    public List<ExpenseSummary> getReasonSummary(){
        return detailRepository.getReasonSummary();
    }

    public Integer getTotalExpense(){
        Integer total = detailRepository.getTotalExpense();
        return total != null ? total : 0;
    }
}
