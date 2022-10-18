package com.example.srot.web.controller.exceptions;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.service.exceptions.*;
import com.example.srot.data.exceptions.IncompleteKYCException;
import com.example.srot.data.exceptions.InvestorNotFoundException;
import com.example.srot.data.exceptions.ListingException;
import com.example.srot.data.exceptions.WalletBalanceInsufficientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.srot.business.domain.DefaultResponse.Status.FAILED;
import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@ControllerAdvice
public class CustomControllerAdvisor {

    @ExceptionHandler(NoOtpProvidedException.class)
    public ResponseEntity<DefaultResponse> handleNoOtpException(NoOtpProvidedException ex){
        DefaultResponse response = new DefaultResponse(SUCCESS, ex.getMessage());
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(ForgetPasswordException.class)
    public ResponseEntity<DefaultResponse> handleNoOtpException(ForgetPasswordException ex){
        DefaultResponse response = new DefaultResponse(SUCCESS, ex.getMessage());
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultResponse> handleNoOtpException(NotFoundException ex){
        DefaultResponse response = new DefaultResponse(FAILED, ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }
    @ExceptionHandler(JWTException.class)
    public ResponseEntity<DefaultResponse> handleJWTException(JWTException ex){
        DefaultResponse response = new DefaultResponse(FAILED,ex.getMessage());
        return ResponseEntity.status(403).body(response);
    }
    @ExceptionHandler(UserInfoException.class)
    public ResponseEntity<DefaultResponse> handleNewUserException(UserInfoException ex) {
        DefaultResponse response = new DefaultResponse(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(QuestionnaireException.class)
    public ResponseEntity<DefaultResponse> handleQuestionnaireException(QuestionnaireException ex) {
        DefaultResponse response = new DefaultResponse(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<DefaultResponse> handleUploadException(S3Exception ex){
        DefaultResponse response = new DefaultResponse(FAILED, ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(InvestorNotFoundException.class)
    public ResponseEntity<DefaultResponse> handleInvestorNotFoundException(InvestorNotFoundException exception) {
        DefaultResponse response = new DefaultResponse(FAILED, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WalletBalanceInsufficientException.class)
    public ResponseEntity<DefaultResponse> handleWalletBalanceInsufficientException(WalletBalanceInsufficientException
                                                                                                exception) {
        DefaultResponse response = new DefaultResponse(FAILED, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IncompleteKYCException.class)
    public ResponseEntity<DefaultResponse> handleIncompleteKYCException(IncompleteKYCException exception) {
        DefaultResponse response = new DefaultResponse(FAILED, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    public ResponseEntity<DefaultResponse> handleListingException(ListingException exception) {
        DefaultResponse response = new DefaultResponse(FAILED, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}