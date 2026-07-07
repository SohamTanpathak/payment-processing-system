package com.sohamtanpathak.fintech.payment_processing_system.payment.service.impl;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.OrderStatus;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

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

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(), request.orderId(), merchantId, order.getAmount(), request.method(), request.methodDetails());
        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setPreocessorReference(pending.registrationReference());
            case PaymentResult.Failure failure -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
        }

        payment = paymentRepository.save(payment);
        orderRepository.save(order);

        return paymentMapper.toResponse(payment);
    }
}








