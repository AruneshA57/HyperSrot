package com.example.srot.business.domain;

import com.example.srot.data.model.Transaction;
import com.example.srot.data.utils.CurrencyConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDisplay {
    private String profileId;
    private String transId;
    private String particulars;
    private String type;
    private String status;
    private Long amount;
    private String currentBalance;
    private String date;
    private String modeOfPayment;

    public TransactionDisplay(String id, Transaction transaction, String type) {
        this.profileId = id;
        this.status = transaction.getStatus();
        this.amount = transaction.getAmount();
        this.date = transaction.getTimestamp().toString();
        this.transId = transaction.getId().toString();
        this.particulars = transaction.getParticulars();
        this.type = type;
        this.currentBalance = CurrencyConverter.paiseLongToRupeeString(transaction.getWallet().getWalletBalance());
        this.modeOfPayment = transaction.getMode();
    }
}
