package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "detail_temp")
@Getter
@Setter
public class DetailTemp {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDate billingDate;

    @Column
    private String reason;

    @Column
    private String transportation;

    @Column
    private String roundTrip;

    @Column
    private Integer amount;

    @Column
    private Integer subtotal;

    @Column
    private String remark;

    @Column
    private String fileName;

    @Column
    private Integer userId;

    @Column
    private String slipTempKey;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;

}
