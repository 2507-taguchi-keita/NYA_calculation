package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "slips")
@Getter
@Setter
public class Slip {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer status;

    @Column
    private Integer step;

    @Column(insertable = false)
    private Integer totalAmount;

    @Column
    private Integer userId;

    @Column
    private Integer approverId;

    @Column
    private LocalDateTime applicationDate;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;

    @Transient  // DBのカラムに対応しないフィールド
    private String statusLabel;

    @Transient
    private String stepLabel;

    @Transient
    private String departmentName;

    @Transient
    private String userName;

    @Transient
    private String userAccount;;
}
