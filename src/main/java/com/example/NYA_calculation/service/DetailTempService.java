package com.example.NYA_calculation.service;

import com.example.NYA_calculation.controller.form.DetailTempForm;
import com.example.NYA_calculation.converter.DetailTempConverter;
import com.example.NYA_calculation.repository.DetailTempRepository;
import com.example.NYA_calculation.repository.entity.DetailTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DetailTempService {

    @Autowired
    DetailTempRepository detailTempRepository;
    @Autowired
    DetailTempConverter detailTempConverter;

    public void addDetailTemp(DetailTempForm detailTempForm) throws IOException {
        detailTempRepository.insert(detailTempConverter.toEntity(detailTempForm));
    }

    public List<DetailTemp> getTempDetails(String tempKey) {
        return detailTempRepository.findByTempKey(tempKey);
    }

    public void removeTemp(String tempKey) {
        detailTempRepository.deleteByTempKey(tempKey);
    }

}
