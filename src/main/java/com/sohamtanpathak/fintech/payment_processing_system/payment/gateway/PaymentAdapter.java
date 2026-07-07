package com.sohamtanpathak.fintech.payment_processing_system.payment.gateway;

import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}
