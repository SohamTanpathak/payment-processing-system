package com.sohamtanpathak.fintech.payment_processing_system.payment.config;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentMethod;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy.CardPaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy.NetBankingPaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){

        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor(),
                PaymentMethod.NETBANKING, new NetBankingPaymentProcessor()

        );
    }
}
