package com.example.srot.business.service;

import com.example.srot.business.domain.OrderDisplay;
import com.example.srot.business.domain.TransactionDisplay;
import com.example.srot.business.dto.GetWalletDto;
import com.example.srot.business.dto.OrderDto;
import com.example.srot.business.domain.WalletDisplay;
import com.example.srot.business.dto.WithdrawDto;
import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.exceptions.*;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.*;
import com.razorpay.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SignatureException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WalletService {

    private final InvestorRepository investorRepository;
    private final WalletRepository walletRepository;
    private final RazorpayCreditTransactionRepository razorpayCreditTransactionRepository;
    private final RazorpayDebitTransactionRepository razorpayDebitTransactionRepository;
    private final AuthenticationService authenticationService;

    @Value("${srot.rzpay.apiKey}")
    private String apiKey;
    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    public WalletService(InvestorRepository investorRepository,
                         WalletRepository walletRepository,
                         PaymentOrderRepository paymentOrderRepository,
                         RazorpayCreditTransactionRepository razorpayCreditTransactionRepository,
                         RazorpayDebitTransactionRepository razorpayDebitTransactionRepository,
                         AuthenticationService authenticationService) {
        this.investorRepository = investorRepository;
        this.walletRepository = walletRepository;
        this.razorpayCreditTransactionRepository = razorpayCreditTransactionRepository;
        this.razorpayDebitTransactionRepository = razorpayDebitTransactionRepository;
        this.authenticationService = authenticationService;
    }

    private boolean verifyPaymentSignature(String orderId, String paymentId, String signature)
            throws RazorpayException {

        JSONObject options = new JSONObject();

        try {
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_signature", signature);
            return Utils.verifyPaymentSignature(options, this.secretKey);
        } catch (RazorpayException e) {
            throw new RazorpayException(e);
        }
    }

    @Transactional
    public WalletDisplay getWallet(GetWalletDto getWalletDto) throws InvestorNotFoundException {
        //Long investorId = Long.valueOf(getWalletDto.getInvestorId()); to be removed
        Investor investor = authenticationService.findAuthenticatedInvestor();

        //Investor investor = investorRepository.findById(investorId). to be removed
         //       orElseThrow(() -> new InvestorNotFoundException(Long.valueOf(getWalletDto.getInvestorId())));

        Integer duration = (getWalletDto.getDuration() == null) ? 90 : Integer.parseInt(getWalletDto.getDuration());
        String type = (getWalletDto.getType() == null) ? "ALL" : getWalletDto.getType();

        Wallet wallet = investor.getWallet();
        return new WalletDisplay(wallet, duration, type);
    }

    @Transactional
    public OrderDisplay createOrder(Long investorId, Long amount) throws RazorpayException, InvestorNotFoundException {
        Optional<Investor> investorOptional = investorRepository.findById(investorId);

        if(investorOptional.isEmpty()) {
            throw new InvestorNotFoundException(investorId);
        }

        Investor investor = investorOptional.get();
        Wallet wallet = investor.getWallet();

        RazorpayCreditTransaction rzpayCreditTransaction = new RazorpayCreditTransaction();
        rzpayCreditTransaction.setAmount(amount);
        rzpayCreditTransaction.setWallet(wallet);

        //save transaction to generate id
        rzpayCreditTransaction = razorpayCreditTransactionRepository.save(rzpayCreditTransaction);

        var razorpayClient = new RazorpayClient(apiKey, secretKey);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("currency", "INR");
        jsonObject.put("receipt", rzpayCreditTransaction.getId().toString());

        //creating new order
        Order order = razorpayClient.Orders.create(jsonObject);

        rzpayCreditTransaction.setOrderId(order.get("id"));
        rzpayCreditTransaction.setNumOfAttempts(order.get("attempts"));
        rzpayCreditTransaction.setOrderTimestamp(new Date(((java.util.Date)order.get("created_at")).getTime()));
        rzpayCreditTransaction.setTimestamp(rzpayCreditTransaction.getOrderTimestamp());
        rzpayCreditTransaction.setStatus("created");

        rzpayCreditTransaction = razorpayCreditTransactionRepository.save(rzpayCreditTransaction);
        wallet.addRazorpayCreditTransaction(rzpayCreditTransaction);
        walletRepository.save(wallet);

        return new OrderDisplay(rzpayCreditTransaction);
    }

    @Transactional
    public void updateOrderAndPayment(RazorpayCreditTransaction razorpayCreditTransaction,
                                         Order order, Payment payment) {

        //set order details
        razorpayCreditTransaction.setStatus(order.get("status"));
        razorpayCreditTransaction.setAmountPaid(Long.parseLong(order.get("amount_paid").toString()));
        razorpayCreditTransaction.setNumOfAttempts(order.get("attempts"));

        //set payment details
        razorpayCreditTransaction.setPaymentId(payment.get("id"));
        razorpayCreditTransaction.setTimestamp(new Date(((java.util.Date) payment.get("created_at")).getTime()));
        razorpayCreditTransaction.setMode(payment.get("method"));

        razorpayCreditTransaction.setParticulars("Credit to wallet from bank account");

        Wallet wallet = razorpayCreditTransaction.getWallet();
        wallet.setWalletBalance(wallet.getWalletBalance() + razorpayCreditTransaction.getAmount());

        razorpayCreditTransaction = razorpayCreditTransactionRepository.save(razorpayCreditTransaction);
        log.info("Saved Razorpay credit transaction: " + razorpayCreditTransaction);
    }

    @Transactional
    public WalletDisplay updateOrder(OrderDto orderDto)
            throws RazorpayException, SignatureException, SignatureMismatchException, PaymentInfoNotFoundException {

        RazorpayCreditTransaction razorpayCreditTransaction =
                razorpayCreditTransactionRepository.findByOrderId(orderDto.getOrderId())
                        .orElseThrow(() -> new PaymentInfoNotFoundException(orderDto.getOrderId()));

        try {
            if (!verifyPaymentSignature(razorpayCreditTransaction.getOrderId(), orderDto.getPaymentId(),
                    orderDto.getSignature())) {
                throw new SignatureMismatchException(razorpayCreditTransaction.getOrderId());
            }
        } catch (RazorpayException exception) {
            throw new RazorpayException(exception);
        }

        log.info("Signature verified successfully");

        RazorpayClient razorpayClient = new RazorpayClient(apiKey, secretKey);
        /*Order order = razorpayClient.Orders.fetch(razorpayCreditTransaction.getOrderId());
        Payment payment = razorpayClient.Payments.fetch(orderDto.getPaymentId());*/

        updateOrderAndPayment(razorpayCreditTransaction,
                razorpayClient.Orders.fetch(razorpayCreditTransaction.getOrderId()) ,
                razorpayClient.Payments.fetch(orderDto.getPaymentId()));

        return new WalletDisplay(razorpayCreditTransaction.getWallet());
    }

    @Transactional
    public TransactionDisplay withdrawFunds(WithdrawDto withdrawDto) {
       // Investor investor = investorRepository.findById(Long.parseLong(withdrawDto.getInvestorId())).
       //         orElseThrow(() -> new InvestorNotFoundException(Long.parseLong(withdrawDto.getInvestorId())));
        Investor investor = authenticationService.findAuthenticatedInvestor();

        if(investor.getBankKYC() == null || !investor.getBankKYC().getStatus().equals(KYCStatus.COMPLETE)) {
           // throw new IncompleteKYCException("Bank KYC Incomplete for id: " + withdrawDto.getInvestorId());
            throw new IncompleteKYCException("Bank KYC Incomplete for id: " + investor.getId());
        }

        if(investor.getWallet().getWalletBalance() < Long.parseLong(withdrawDto.getAmount())) {
            throw new WalletBalanceInsufficientException(withdrawDto.getAmount());
        }

        Wallet wallet = investor.getWallet();

        RazorpayDebitTransaction razorpayDebitTransaction = new RazorpayDebitTransaction();
        razorpayDebitTransaction.setAmount(Long.parseLong(withdrawDto.getAmount()));
        razorpayDebitTransaction.setRequestTimestamp(new Date(Calendar.getInstance().getTime().getTime()));
        razorpayDebitTransaction.setTimestamp(razorpayDebitTransaction.getRequestTimestamp());
        razorpayDebitTransaction.setStatus("processing");
        razorpayDebitTransaction.setMode("NEFT");
        razorpayDebitTransaction.setParticulars("Wallet withdrawal");
        razorpayDebitTransaction.setIdempotencyKey(UUID.randomUUID().toString());

        razorpayDebitTransaction.setWallet(wallet);
        razorpayDebitTransaction = razorpayDebitTransactionRepository.save(razorpayDebitTransaction);

        wallet.addRazorpayDebitTransaction(razorpayDebitTransaction);
        wallet = walletRepository.save(wallet);

        // here can we removed the assignment wallet.
        return new TransactionDisplay(investor.getId().toString(), razorpayDebitTransaction, "DEBIT");
    }
}
