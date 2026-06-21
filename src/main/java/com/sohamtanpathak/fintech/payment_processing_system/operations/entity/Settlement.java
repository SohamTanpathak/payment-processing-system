package com.sohamtanpathak.fintech.payment_processing_system.operations.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.SettlementStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Settlement happens every day. Settlement is created for all the payments captured on last day.
 * With Settlement, we actually send the money from our nodal account over to the merchant's accounts.
 *
 * A settlement can have multiple payments, and a payment can be a part of multiple settlements(rare, but it happens sometimes)
 *
 * */

@Entity
@Table(name = "settlement")
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchant;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gross_amount_units", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "gross_amount_currency", nullable = false))
    })
    private Money grossAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "refund_amount_units", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "refund_amount_currency", nullable = false))
    })
    private Money refundAmount;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "fee_amount_units", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "fee_amount_currency", nullable = false))
    })
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gst_amount_units", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "gst_amount_currency", nullable = false))
    })
    private Money gstAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "net_amount_units", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "net_amount_currency", nullable = false))
    })
    private Money netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementStatus status;

    @Column(nullable = false, length = 50)
    private String bankReference;

    private LocalDateTime processedAt;
}
