package com.sohamtanpathak.fintech.payment_processing_system.payment.service.impl;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.OrderStatus;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentEvent;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentStatus;
import com.sohamtanpathak.fintech.payment_processing_system.common.exceptions.BusinessRuleViolationException;
import com.sohamtanpathak.fintech.payment_processing_system.common.exceptions.ResourceNotFoundException;
import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.request.PaymentInitRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.PaymentResponse;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.OrderRecord;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.Payment;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.PaymentGatewayRouter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.gateway.dto.PaymentResult;
import com.sohamtanpathak.fintech.payment_processing_system.payment.mapper.PaymentMapper;
import com.sohamtanpathak.fintech.payment_processing_system.payment.repository.OrderRepository;
import com.sohamtanpathak.fintech.payment_processing_system.payment.repository.PaymentRepository;
import com.sohamtanpathak.fintech.payment_processing_system.payment.service.PaymentService;
import com.sohamtanpathak.fintech.payment_processing_system.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        // check if order exists for the merchantId
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        //order can be processed only if payment status is either in 'created' or 'attempted' state
        if(order.getOrderStatus() != OrderStatus.CREATED || order.getOrderStatus() != OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER NOT PAYABLE",
                    "Order cannot accept payment status: "+ order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        //Now create a payment object
        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(), merchantId,
                order.getAmount(), request.method(),
                request.methodDetails());
        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setPreocessorReference(pending.registrationReference());
            case PaymentResult.Failure failure -> {
//                payment.setStatus(PaymentStatus.FAILED); (done via statemachine, refer below line)
                paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
            case PaymentResult.Success success -> {
                log.warn("Invalid state");
                return null;
            }
        }

        payment = paymentRepository.save(payment);
        orderRepository.save(order);
        
        // TODO: send an outbox (kafka event)

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

//        payment.setStatus(PaymentStatus.CAPTURING); (done via statemachine, refer below line)
        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), payment.getId());

        if(paymentResult instanceof PaymentResult.Success success){
//            payment.setStatus(PaymentStatus.CAPTURED); (done via statemachine, refer below line)
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured, paymentId: {}", paymentId);
        } else if(paymentResult instanceof PaymentResult.Failure failure){
//            payment.setStatus(PaymentStatus.AUTHORIZED); (done via statemachine, refer below line)
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
            log.warn("Payment captured failed, paymentId: {}",paymentId);
        }

        payment = paymentRepository.save(payment);

        // TODO: send an outbox (kafka event)

        return paymentMapper.toResponse(payment);
    }
}








