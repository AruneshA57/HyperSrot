package com.example.srot.business.scheduledJobs;

import com.example.srot.business.service.WalletService;
import com.example.srot.data.model.RazorpayCreditTransaction;
import com.example.srot.data.repository.RazorpayCreditTransactionRepository;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PaymentScheduler {

    /*@Scheduled(fixedDelay = 2000L)
    public void something() {
        log.info("Inside something");
    }*/

    @Value("${srot.rzpay.apiKey}")
    private String apiKey;
    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    private final WalletService walletService;
    private final RazorpayCreditTransactionRepository razorpayCreditTransactionRepository;

    public PaymentScheduler(WalletService walletService,
                            RazorpayCreditTransactionRepository razorpayCreditTransactionRepository) {
        this.walletService = walletService;
        this.razorpayCreditTransactionRepository = razorpayCreditTransactionRepository;
    }

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void updateRazorpayCreditTransactionStatus() {
        log.info("Checking Razorpay credit transactions");
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secretKey);

            Iterable<RazorpayCreditTransaction> razorpayCreditTransactions =
                razorpayCreditTransactionRepository.findAllByStatus("created");
            razorpayCreditTransactions.forEach(razorpayCreditTransaction -> {

                try {
                    log.info("Updating transaction: " + razorpayCreditTransaction);
                    Order order = razorpayClient.Orders.fetch(razorpayCreditTransaction.getOrderId());
                    List<Payment> payments = razorpayClient.Orders.
                            fetchPayments(razorpayCreditTransaction.getOrderId());

                    if(payments.isEmpty()) {
                        log.info("No payment info found for transaction: " + razorpayCreditTransaction);
                    }
                    else {
                        walletService.updateOrderAndPayment(razorpayCreditTransaction, order, payments.get(0));
                    }
                } catch (RazorpayException e) {
                    log.error("Razorpay Exception occurred while fetching order and payment info: " + e);
                    e.printStackTrace();
                }
            });
        } catch (RazorpayException e) {
            log.error("Razorpay Exception occurred while creating Razorpay client: " + e);
            e.printStackTrace();
        }
    }
}
