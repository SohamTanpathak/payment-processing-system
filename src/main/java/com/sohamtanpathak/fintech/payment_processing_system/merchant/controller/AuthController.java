package com.sohamtanpathak.fintech.payment_processing_system.merchant.controller;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.MerchantSignupRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.MerchantResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.service.AuthService;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * This will contain APIs related to authentication and signing in.
 */

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MerchantResponse> signup(@RequestBody @Valid MerchantSignupRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.signup(request)
        );
    }
}
