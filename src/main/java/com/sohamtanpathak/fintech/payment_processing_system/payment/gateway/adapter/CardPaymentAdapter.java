package com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.adapter;

import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.PaymentAdapter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentResult;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;
import com.sohamtanpathak.fintech.payment_processing_system.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest request){

        String token = (String) request.methodDetails().get("token");

        /*
          Only vault service has the access to te real pan, so we call vaultService
        */
        PaymentProcessorResponse response = vaultService.charge(
            request.paymentId(), token, request.amount(), request.methodDetails()
        );
        return switch (response){
            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }
}
