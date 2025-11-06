package com.example.NYA_calculation.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String account;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private Integer departmentId;

    @Column
    private Integer authority;

    @Column
    private boolean isStopped;

    @Column
    private Integer approverId;

    @Column
    private Timestamp createdDate;

    @Column
    private Timestamp updatedDate;
}
