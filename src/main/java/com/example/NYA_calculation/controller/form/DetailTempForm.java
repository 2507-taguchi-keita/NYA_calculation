package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class DetailTempForm {
    private Integer id;
    private LocalDate billingDate;
    private String reason;
    private String transportation;
    private String roundTrip;
    private Integer amount;
    private Integer subtotal;
    private String remark;
    private String fileName;
    private Integer userId;
    private String slipTempKey;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    // 画像添付用
    private MultipartFile file;
}
