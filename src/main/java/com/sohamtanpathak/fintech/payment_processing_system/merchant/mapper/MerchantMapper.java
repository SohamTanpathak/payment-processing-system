package com.sohamtanpathak.fintech.payment_processing_system.merchant.mapper;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request.MerchantSignupRequest;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.MerchantResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromSignUpRequest(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
