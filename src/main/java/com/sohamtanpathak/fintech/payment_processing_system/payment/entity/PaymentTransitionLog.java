package com.sohamtanpathak.fintech.payment_processing_system.payment.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentActor;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentEvent;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * Logs whenever something happens to the Payment - status changes, etc
 */

@Entity
@Table(name = "payment_transition_log", indexes = {
        @Index(name = "idx_payment_transition_log_payment_id", columnList = "payment_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTransitionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false, length = 30)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 30)
    private PaymentEvent event;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private PaymentStatus toStatus;

    @Column(name = "actor", length = 100)
    private PaymentActor actor; // who changed the payment status, mostly it will be system only

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
}
