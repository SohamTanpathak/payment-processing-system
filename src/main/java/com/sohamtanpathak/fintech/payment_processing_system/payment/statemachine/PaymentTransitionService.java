package com.sohamtanpathak.fintech.payment_processing_system.payment.statemachine;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentActor;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentEvent;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentStatus;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.Payment;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.PaymentTransitionLog;
import com.sohamtanpathak.fintech.payment_processing_system.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService{

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event){
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        payment.setStatus(next);

        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM) //TODO: fetch merchant context to identify actor
                .occurredAt(LocalDateTime.now())
                .build();

        paymentTransitionLogRepository.save(log);

        return next;
    }
}
