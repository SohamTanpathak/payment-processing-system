package com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy;

import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;

public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        // make third party calls on basis of card company
        return null;
    }
}
