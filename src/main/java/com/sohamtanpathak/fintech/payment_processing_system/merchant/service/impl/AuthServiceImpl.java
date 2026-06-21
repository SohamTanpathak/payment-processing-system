package com.sohamtanpathak.fintech.payment_processing_system.merchant.service.impl;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.MerchantStatus;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.UserRole;
import com.sohamtanpathak.fintech.payment_processing_system.common.exceptions.DuplicateResourceException;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.MerchantSignupRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.MerchantResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.AppUser;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.Merchant;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.mapper.MerchantMapper;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.repository.AppUserRepository;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.repository.MerchantRepository;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {

        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: "+ request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchant = merchantRepository.save(merchant);

        // When one merchant gets onboarded, app user for that merchant is going to be created\
        // with the email and password they have defined in the MerchantSignup request

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) //TODO: encrypt using Bcrypt
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return merchantMapper.toResponse(merchant);
    }
}










