package com.example.NYA_calculation.controller.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;

import static com.example.NYA_calculation.validation.ErrorMessages.*;

@Getter
@Setter
public class DetailForm {

    private Integer id;

    @NotNull(message = E0007)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate billingDate;

    private String roundTrip;

    @NotBlank(message = E0009)
    @Pattern(regexp = "^[0-9]*$", message = E0011)
    private String amount;

    private Integer subtotal;

    @NotEmpty(message = E0008)
    private String reason;

    @NotEmpty(message = E0010)
    private String transportation;

    private String fileName;

    private Integer slipId;

    @Size(max = 50, message = E0012)
    private String remark;
    private Integer userId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    // 画像添付用
    private MultipartFile file;
}
