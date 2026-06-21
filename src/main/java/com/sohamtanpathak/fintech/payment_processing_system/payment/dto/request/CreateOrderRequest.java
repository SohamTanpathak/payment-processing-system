package com.sohamtanpathak.fintech.payment_processing_system.payment.dto.request;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt, // order-id (known to merchant) (our system don't know what this order-id means)

        Map<String, Object> notes,

        LocalDateTime expiresAt
) {
}
