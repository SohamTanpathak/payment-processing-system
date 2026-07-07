package com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(

        PaymentMethod method,
        Money amount,
        Map<String, Object> methodDetails
) {
}
