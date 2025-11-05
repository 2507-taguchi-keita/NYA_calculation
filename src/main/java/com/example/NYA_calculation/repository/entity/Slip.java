package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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

    @Column
    private Integer totalAmount;

    @Column
    private Integer userId;

    @Column
    private Integer approverId;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;
}
