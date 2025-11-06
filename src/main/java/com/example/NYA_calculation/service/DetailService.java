package com.example.NYA_calculation.service;

import com.example.NYA_calculation.dto.ExpenseSummary;
import com.example.NYA_calculation.repository.DetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailService {

    @Autowired
    DetailMapper detailMapper;

    public List<ExpenseSummary> getMonthlyTotal() {
        return detailMapper.getMonthlyTotal();
    }
}
