package com.sohamtanpathak.fintech.payment_processing_system.vault.service;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;
import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.request.TokenizeRequest;
import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(@Valid TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
