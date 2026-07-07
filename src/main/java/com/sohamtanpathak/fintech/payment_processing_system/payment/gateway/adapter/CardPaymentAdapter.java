package com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.adapter;

import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.PaymentAdapter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentResult;

public class CardPaymentAdapter implements PaymentAdapter {

    @Override
    public PaymentResult initiate(PaymentRequest request){

        return null;
    }
}
