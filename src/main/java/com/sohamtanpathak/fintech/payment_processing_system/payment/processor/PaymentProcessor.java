package com.sohamtanpathak.fintech.payment_processing_system.payment.processor;

import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
