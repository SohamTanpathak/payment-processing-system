package com.sohamtanpathak.fintech.payment_processing_system.merchant.service.impl;

import com.sohamtanpathak.fintech.payment_processing_system.common.exceptions.ResourceNotFoundException;
import com.sohamtanpathak.fintech.payment_processing_system.common.util.RandomizerUtil;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.CreateApiKeyRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyCreateResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.ApiKey;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.Merchant;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.mapper.ApiKeyMapper;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.repository.ApiKeyRepository;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.repository.MerchantRepository;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.service.ApiKeyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() ->new ResourceNotFoundException("merchant", merchantId));

        String keyId = "pps_"+request.environment().name().toUpperCase()+"_"+ RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) //TODO: encode with BcryptPassword encoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        key.setEnabled(false);  //revoking the key
        apiKeyRepository.save(key);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        if(!apiKey.isEnabled()) throw new RuntimeException("Cannot rotate a disabled key");

        String newRawSecret = RandomizerUtil.randomBase64(40);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());  // previous one will be the current secret hash
        apiKey.setKeySecretHash(newRawSecret); //TODO: encode with BcryptPassword encoder
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), newRawSecret, apiKey.getEnvironment());
    }


}
