package com.sohamtanpathak.fintech.payment_processing_system.payment.mapper;

import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.PaymentResponse;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id") //In Payment we have order and that order has id, we are mapping that id with the orderId of PaymentResponse
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponse> toResponseList(List<Payment> paymentList);


}
