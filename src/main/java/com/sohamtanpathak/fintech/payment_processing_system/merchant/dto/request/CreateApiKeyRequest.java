package com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.Environment;

public record CreateApiKeyRequest(

        Environment environment

) {
}
