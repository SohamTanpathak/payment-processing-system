package com.sohamtanpathak.fintech.payment_processing_system.vault.dto.response;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.CardBrand;

public record TokenizeResponse(

        String token,

        String lastFour,

        CardBrand brand,

        Integer expiryMonth,

        Integer expiryYear
) {
}
