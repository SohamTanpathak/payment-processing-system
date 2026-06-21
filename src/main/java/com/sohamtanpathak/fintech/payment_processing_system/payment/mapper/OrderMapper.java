package com.sohamtanpathak.fintech.payment_processing_system.payment.mapper;

import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.OrderResponse;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {


    OrderResponse toOrderResponse(OrderRecord orderRecord);

}
