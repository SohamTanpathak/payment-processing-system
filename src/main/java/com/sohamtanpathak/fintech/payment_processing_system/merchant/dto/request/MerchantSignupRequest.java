package com.sohamtanpathak.fintech.payment_processing_system.merchant.dto.request;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(

        @NotNull(message = "Name is required.")
        @Size(max = 50, message = "Name should not exceed more than 50 characters.")
        String name,

        @Email(message = "Email is invalid")
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 characters long")
        String password,

        @Size(max = 50, message = "Business Name should not exceed 50 characters")
        String businessName,

        BusinessType businessType

){
}
