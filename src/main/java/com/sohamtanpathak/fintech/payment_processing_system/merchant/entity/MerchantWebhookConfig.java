package com.sohamtanpathak.fintech.payment_processing_system.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * MerchantWebhookConfig is used to communicate between our system's server and merchant's server
 * whenever any event takes place
 * */

@Entity
@Table(name = "merchant_webhook_config")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantWebhookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    // it is the merchant's website. Eg: www.zara.com/webhook/success
    @Column(nullable = false, length = 500)
    private String targetUrl;

    // we can use this field to sign the webhook for security purpose
    @Column(length = 255)
    private String webhookSecretHash;

    @Column(nullable = false)
    private Boolean enabled = true;  // enable or disable the event


    // comma-separated list of event types to subscribe to.
    //Eg: ["order.created", "payment.captured"]
    @Column(length = 255)
    private String eventTypes;

}
