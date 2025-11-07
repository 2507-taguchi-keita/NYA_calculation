package com.example.NYA_calculation.repository;

import com.example.NYA_calculation.repository.entity.DetailTemp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailTempRepository {

    void insert(DetailTemp detailTemp);

    List<DetailTemp> findByTempKey(String slipTempKey);

    void deleteByTempKey(String slipTempKey);

}
