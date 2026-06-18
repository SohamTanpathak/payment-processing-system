package com.sohamtanpathak.fintech.payment_processing_system.vault.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Card Token is an entity using which we interact with the card for a merchant.
 * A user might have already entered their card details in our system, but when they enter it from
 * Zara, we show a card token by Zara, hence card token should have a reference to the merchant_id.
 *
 * For the same card, two tokens can be generated (lets say one for Zara and one for H&M).
 * Zara token can't be used by H&M and vice-versa
 */

@Entity
@Table(name = "card_token")
public class CardToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false)
    private VaultCard vaultCard;

    @Column(nullable = false)
    private UUID customer;

    @Column(nullable = false)
    private UUID merchant;

    private LocalDateTime revokedAt;

}
