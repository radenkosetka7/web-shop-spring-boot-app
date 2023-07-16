package com.example.webshop.models.entities;

import javax.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "logger")
public class LoggerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
    @Basic
    @Column(name = "level", nullable = false, length = 45)
    private String level;
    @Basic
    @Column(name = "log", nullable = false, length = 1000)
    private String log;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

}
