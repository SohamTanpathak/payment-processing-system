package com.sohamtanpathak.fintech.payment_processing_system.vault.entity;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.BaseEntity;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.CardBrand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaultCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String lastFour;

    @Column(nullable = false, length = 6)
    private String bin; // brand(of card) identification number (first six digits of card)

    @Column(nullable = false)
    private byte[] encryptedPan; //PAN no. encrypted and stored in DB

    @Column(nullable = false)
    private byte[] encryptedDek; //Dek is the string that is used to encrypt the PAN

    @Column(nullable = false)
    private CardBrand brand;  // VISA, RUPAY, etc

    @Column(nullable = false)
    private String expiryMonth;

    @Column(nullable = false)
    private String expiryYear;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;
}
