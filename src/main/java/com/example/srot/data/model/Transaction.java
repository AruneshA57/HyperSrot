package com.example.srot.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class Transaction extends BaseEntity {
    private String particulars;
    private Long amount;
    private Date timestamp;
    private String status;
    private String mode;

    @ManyToOne
    private Wallet wallet;

    @Override
    public String toString() {
        return "Transaction{" +
                "particulars='" + particulars + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
