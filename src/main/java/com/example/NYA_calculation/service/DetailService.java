package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailForm;
import com.example.NYA_calculation.converter.DetailConverter;
import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.DetailRepository;
import com.example.NYA_calculation.repository.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
