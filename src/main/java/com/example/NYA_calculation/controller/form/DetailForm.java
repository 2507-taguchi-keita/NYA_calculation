package com.example.NYA_calculation.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class DetailForm {
    private Integer id;
    private LocalDate billingDate;
    private String roundTrip;
    private Integer amount;
    private Integer subtotal;
    private String reason;
    private String transportation;
    private String fileName;
    private Integer slipId;
    private String remark;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    // 画像添付用
    private MultipartFile file;
}
