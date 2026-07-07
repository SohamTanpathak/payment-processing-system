package com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(

        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
