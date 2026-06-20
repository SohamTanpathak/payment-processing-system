package com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.BusinessType;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(

        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus


) {
}
