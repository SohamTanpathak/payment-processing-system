package com.sohamtanpathak.fintech.payment_processing_system.merchant.mapper;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyCreateResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.response.ApiKeyResponse;
import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyCreateResponse toCreateResponse(ApiKey apiKey);

    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
