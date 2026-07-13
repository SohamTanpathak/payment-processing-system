package com.sohamtanpathak.fintech.payment_processing_system.payment.service;

import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.request.PaymentInitRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);

    void resolveAuthorization(UUID paymentId, boolean approve, String bankRef, String errorCode, String errorDescription);
}
