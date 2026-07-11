package com.sohamtanpathak.fintech.payment_processing_system.payment.processor.strategy;

import com.sohamtanpathak.fintech.payment_processing_system.common.util.RandomizerUtil;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessor;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        //third party calls

        // its bank code fail simulation in which you provide us a bank code in which you basically want
        // us to fail everything
        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("BANK").toString() : null;


        // simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_"+ RandomizerUtil.randomBase64(16);

        //Bank gives it to us so we can redirect client to this link and there theyt make the payment, eg: ICICI bank link or Canara bank link
        // once the payment is done on the bank side, the bank will notify us via the webhook
        String redirectRef = "http://REDIRECT_BANK.com/"+processorRef;

        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}
