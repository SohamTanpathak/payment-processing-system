package com.sohamtanpathak.fintech.payment_processing_system.merchant.service;


import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.CreateApiKeyRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyCreateResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {


    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
