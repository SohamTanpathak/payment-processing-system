package com.sohamtanpathak.fintech.payment_processing_system.payment.gateway;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;
import com.sohamtanpathak.fintech.payment_processing_system.payment.config.PaymentAdapterConfig;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public PaymentResult initiate(PaymentRequest request){

        //Based on the method selected by user, it will give the appropriate adapter
        //Eg: if user requested method: Card ,then CardPaymentAdapter will be fetched from the Map and used.
        PaymentAdapter adapter = paymentAdapters.get(request.method());

        if(adapter == null)
            throw new IllegalArgumentException("No payment adapter registered for method: "+ request.method());


        return adapter.initiate(request);


    }
}
