package com.sohamtanpathak.fintech.payment_processing_system.vault.controller;

import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.request.TokenizeRequest;
import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.response.TokenizeResponse;
import com.sohamtanpathak.fintech.payment_processing_system.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;

    UUID merchantId = UUID.fromString("6454922b-ac33-455b-a019-f2b4a847c53b"); //TODO: replace it with MerchantContext

    /**
     * It will take the card rew info and convert it into a token.
     */
    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request, merchantId));
    }
}
