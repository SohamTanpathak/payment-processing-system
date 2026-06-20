package com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(

        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
