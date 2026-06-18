package com.sohamtanpathak.fintech.payment_processing_system.merchant.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.enums.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

/**
 *
 * AppUser is an entity that are a part of a merchant's team (owner, member, admin, etc.)
 */

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
