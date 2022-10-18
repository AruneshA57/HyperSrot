package com.example.srot.web.controller;

import com.example.srot.business.domain.*;
import com.example.srot.business.dto.AddFundDto;
import com.example.srot.business.dto.GetWalletDto;
import com.example.srot.business.dto.OrderDto;
import com.example.srot.business.dto.WithdrawDto;
import com.example.srot.business.service.AuthenticationService;
import com.example.srot.business.service.WalletService;
import com.example.srot.data.exceptions.InvestorNotFoundException;
import com.example.srot.data.exceptions.PaymentInfoNotFoundException;
import com.example.srot.data.exceptions.SignatureMismatchException;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.FAILED;
import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@Slf4j
@RestController
public class InvestorWalletWebServiceController {

    private final WalletService walletService;
    private final AuthenticationService authenticationService;

    @Autowired
    public InvestorWalletWebServiceController(WalletService walletService, AuthenticationService authenticationService) {
        this.walletService = walletService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/investor/wallet/getWallet")
    @ResponseBody
    public ResponseEntity<DefaultResponse> getWallet(
             @RequestParam(value = "duration", defaultValue ="90") String duration,
             @RequestParam(value = "type", defaultValue = "ALL") String type) {

        GetWalletDto getWalletDto = new GetWalletDto(duration,type);
        try {
            WalletDisplay wallet = walletService.getWallet(getWalletDto);

            Map<String, WalletDisplay> data = Map.of("wallet",wallet);
            return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Wallet retrieved successfully"));
        }
        //TODO Check which approach is going to be standardized
        catch(InvestorNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        }
    }

    @PostMapping("/investor/wallet/createOrder")
    @ResponseBody
    public ResponseEntity<DefaultResponse> createOrder(@RequestBody AddFundDto addFundDto) {

        Long investorId  = authenticationService.findAuthenticatedInvestor().getId();
        Long amount = Long.parseLong(addFundDto.getAmount());
        try {
            OrderDisplay orderDisplay = walletService.createOrder(investorId, amount);
            //TODO: send appropriate order fields]
            Map<String, OrderDisplay> data = Map.of("order_display",orderDisplay);
            return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Order created successfully"));
        }
        catch(InvestorNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        }
        catch(RazorpayException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        }
    }

    @PostMapping("/investor/wallet/updateOrder")
    public ResponseEntity<DefaultResponse> updateOrder(@RequestBody OrderDto orderDto) {
        //TODO: Update catch blocks
        try {
            WalletDisplay wallet = walletService.updateOrder(orderDto);
            //TODO: send appropriate order fields
            Map<String,WalletDisplay> data = Map.of("Wallet",wallet);
            return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Order saved successfully"));
        }
        catch(RazorpayException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        } catch (SignatureMismatchException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        } catch (SignatureException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        } catch (PaymentInfoNotFoundException exception) {
            return ResponseEntity.status(404).body(new DefaultResponse(FAILED,exception.getMessage()));
        }
    }

    @PostMapping("/investor/wallet/withdraw")
    public ResponseEntity<DefaultResponse> withdraw(@RequestBody WithdrawDto withdrawDto) {
        TransactionDisplay transactionDisplay = walletService.withdrawFunds(withdrawDto);
        Map<String, TransactionDisplay> data = Map.of("transaction_display",transactionDisplay);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Withdraw completed successfully"));
    }
}
