package com.example.srot.business.domain;

import com.example.srot.data.model.DebitInvestmentTransaction;
import com.example.srot.data.model.RazorpayCreditTransaction;
import com.example.srot.data.model.Transaction;
import com.example.srot.data.model.Wallet;
import com.example.srot.data.utils.CurrencyConverter;
import com.example.srot.data.utils.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class WalletDisplay {
    private String withdrawableBalance;
    private String nonWithdrawableBalance;
    private Set<TransactionDisplay> transactions = new HashSet<>();

    public WalletDisplay(Wallet wallet, Integer duration, String type) {
        this.withdrawableBalance = CurrencyConverter.paiseLongToRupeeString(wallet.getWalletBalance());
        this.nonWithdrawableBalance =
                CurrencyConverter.paiseLongToRupeeString(wallet.getReferralBonus()
                        + wallet.getJoiningBonus() + wallet.getQuestionnaireBonus());

        if(type.equals("ALL") || type.equals("CREDIT")) {
            Set<RazorpayCreditTransaction> rzpayCreditTransactions = wallet.getRzpayCreditTransactions();
            rzpayCreditTransactions.forEach(transaction -> {
                if ((DateUtil.getDaysInBetween(transaction.getTimestamp().toLocalDate(), LocalDate.now())
                        <= duration)) {
                    this.transactions.add(new TransactionDisplay(wallet.getInvestor().getId().toString(),
                            transaction, "CREDIT"));
                }
            });
        }

        if(type.equals("ALL") || type.equals("DEBIT")) {
            Set<DebitInvestmentTransaction> debitInvestmentTransactions = wallet.getDebitInvestmentTransactions();
            debitInvestmentTransactions.forEach(transaction -> {
                if ((DateUtil.getDaysInBetween(transaction.getTimestamp().toLocalDate(), LocalDate.now())
                        <= duration)) {
                    this.transactions.add(new TransactionDisplay(wallet.getInvestor().getId().toString(),
                            transaction, "DEBIT"));
                }
            });
        }
    }

    public WalletDisplay(Wallet wallet) {
        this(wallet, 90, "ALL");
    }
}
