package com.sohamtanpathak.fintech.payment_processing_system.merchant.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Customer is an entity who pay to different merchants using out system.
 * */
@Entity
@Table(name = "customer", indexes = {
        @Index(name = "idx_customer_merchant_id", columnList = "merchant_id"),
        @Index(name = "idx_customer_email", columnList = "email")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //optional=false: providing merchant id is must
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String email; //no unique email for customer, because one customer can belong to multiple merchants like Zara, H&M, etc.

    @Column(length = 20)
    private String contactNumber;

    private LocalDateTime deletedAt;
}
