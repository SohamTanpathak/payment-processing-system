package com.sohamtanpathak.fintech.payment_processing_system.payment.repository;

import com.sohamtanpathak.fintech.payment_processing_system.payment.entity.PaymentTransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransitionLogRepository extends JpaRepository<PaymentTransitionLog, UUID> {


}
