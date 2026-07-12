package com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy;

import com.sohamtanpathak.fintech.payment_processing_system.common.util.RandomizerUtil;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED = "400000000000002";
    public static final String PAN_CARD_EXPIRED = "400000000000069";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        // make third party calls on basis of card company

        String pan = request.pan();

        if(PAN_CARD_DECLINED.equals(pan)){
            log.warn("Card declined");
            return new PaymentProcessorResponse.Failure("CARD_DECLINED", "Card declined by bank");
        }

        if(PAN_CARD_EXPIRED.equals(pan)){
            log.warn("Card expired");
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED", "Card has expired");
        }

        String processorRef = "CARD_PROCESSOR_"+ RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
