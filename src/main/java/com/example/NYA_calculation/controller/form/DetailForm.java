package com.example.NYA_calculation.controller.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import static com.example.NYA_calculation.validation.ErrorMessages.*;

@Getter
@Setter
public class DetailForm {

    private Integer id;
    private String tempId = UUID.randomUUID().toString();

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

    private Integer slipId;

    @Size(max = 50, message = E0012)
    private String remark;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private boolean isNewFromCsv = false;

    private MultipartFile uploadFile;

    private String storedFileName; // 実際に保存したファイル名

    private String originalFileName; // 元のファイル名

    private String fileUrl; // ブラウザで開けるURL

}
