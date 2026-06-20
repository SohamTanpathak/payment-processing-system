package com.sohamtanpathak.fintech.payment_processing_system.merchant.service;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.MerchantSignupRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
