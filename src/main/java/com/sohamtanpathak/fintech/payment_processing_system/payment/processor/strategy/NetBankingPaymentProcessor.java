package com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy;

import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {


        // Call the third party, like the user bank

        return null;
    }
}
