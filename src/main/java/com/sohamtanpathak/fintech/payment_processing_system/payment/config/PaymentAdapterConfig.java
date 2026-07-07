package com.sohamtanpathak.fintech.payment_processing_system.payment.config;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.PaymentAdapter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.adapter.CardPaymentAdapter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.adapter.NetBankingAdapter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethod.CARD, new CardPaymentAdapter(),
                PaymentMethod.NETBANKING, new NetBankingAdapter(),
                PaymentMethod.UPI, new UpiPaymentAdapter()
        );
    }
}
