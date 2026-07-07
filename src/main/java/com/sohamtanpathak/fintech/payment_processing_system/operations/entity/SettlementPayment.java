package com.sohamtanpathak.fintech.payment_processing_system.operations.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "settlement_payment")
public class SettlementPayment extends BaseEntity{

    @EmbeddedId     //@EmbeddedId is used when you are trying to create an Id which contains composite key
    private SettlementPaymentId id;


    /**
     * Using MapsId u can map any field of composite key to any field that you have here.
     * here we are mapping settlemnetId of SettlementPaymentId with this settlement.
     * so this settlement will be same as settlementId of SettlementPaymentId
     */
    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;
}
