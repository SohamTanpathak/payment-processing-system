package com.sohamtanpathak.fintech.payment_processing_system.payment.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_record", indexes = {
        @Index(name = "idx_order_id_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_payment_merchant_id", columnList = "merchant_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    // we are embedding Money class in order_record table
    // in order_record table, we will have two more columns: amountUnits and currency
    @Embedded
    private Money amount;

    @Column(length = 100)
    private String receipt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus orderStatus = OrderStatus.CREATED;  // default value: CREATED

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    /**
     * merchnats can store various type of info related to order in notes.
     * */
    @JdbcTypeCode((SqlTypes.JSON)) // To convert the json getting stored in DB to a Map
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;


    /*
    * If merchant has an expiry for an order, lets say for 10 mins if payment is not done, expire the order.
    */
    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
