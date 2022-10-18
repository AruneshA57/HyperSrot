package com.example.srot.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_codes")
public class EmailCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String code;
    private Instant createdAt;

    public EmailCode(Long userId, Status status, String code) {
        this.userId = userId;
        this.status = status;
        this.code = code;
    }

    public enum Status{
        PENDING,
        CONFIRMED,
        EXPIRED
    }

    @PrePersist
    public void setDate(){
        createdAt = Instant.now();
    }

}
