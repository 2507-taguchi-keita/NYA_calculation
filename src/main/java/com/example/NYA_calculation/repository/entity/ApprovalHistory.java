package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class ApprovalHistory {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer slipId;

    @Column
    private Integer userId;

    @Column
    private Timestamp approvedAt;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;
}
