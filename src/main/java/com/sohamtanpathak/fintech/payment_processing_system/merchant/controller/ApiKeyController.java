package com.sohamtanpathak.fintech.payment_processing_system.merchant.controller;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.CreateApiKeyRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyCreateResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.ApiKey;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId,
                                                       @RequestBody @Valid CreateApiKeyRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId, request));
    }

    // get all api keys for a merchant
    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> listByMerchant(@PathVariable UUID merchantId){
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId){
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }


    /**
     * For a api-key, if merchant want to change its secret hash, then the previous secret hash will
     * also be working till its grace period ends.
     * Later, only the new secret hash will continue to work.
     * This is the rotation of the api-key.
     */
    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotateKey(@PathVariable UUID merchantId, @PathVariable UUID keyId){
        return ResponseEntity.ok(apiKeyService.rotate(merchantId, keyId));
    }
}
