package com.sohamtanpathak.fintech.payment_processing_system.payment.controller;

import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.request.PaymentInitRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.PaymentResponse;
import com.sohamtanpathak.fintech.payment_processing_system.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/v1/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("6454922b-ac33-455b-a019-f2b4a847c53b"); //TODO: replace it with MerchantContext

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId, request));
    }
}
