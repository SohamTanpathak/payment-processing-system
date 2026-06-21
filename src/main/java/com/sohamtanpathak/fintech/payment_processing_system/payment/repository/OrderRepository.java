package com.sohamtanpathak.fintech.payment_processing_system.payment.repository;

import com.sohamtanpathak.fintech.payment_processing_system.payment.dto.response.OrderResponse;
import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.OrderRecord;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {
    boolean existsByMerchantIdAndReceipt(UUID merchantId, @Size(max = 100) String receipt);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);
}
