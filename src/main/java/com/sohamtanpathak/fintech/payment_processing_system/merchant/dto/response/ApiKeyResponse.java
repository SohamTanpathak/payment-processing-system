package com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(

        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt
) {
}
