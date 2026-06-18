package com.sohamtanpathak.fintech.payment_processing_system.operations.entity;

import jakarta.persistence.Embeddable;

import java.util.UUID;

/**
 * This is a composite primary key, which has 2 primary keys: settlementId and paymentId.
 * */

@Embeddable
public class SettlementPaymentId {

    private UUID settlementId;

    private UUID paymentId;
}
