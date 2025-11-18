package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "details")
@Getter
@Setter
public class Detail {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDate billingDate;

    @Column
    private String roundTrip;

    @Column
    private String amount;

    @Column
    private Integer subtotal;

    @Column
    private String reason;

    @Column
    private String transportation;

    @Column
    private Integer slipId;

    @Column
    private String remark;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName; // 実際に保存したファイル名

    @Transient
    private String fileUrl; // 実際に保存したファイル名

}
