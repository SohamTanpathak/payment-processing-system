package com.sohamtanpathak.fintech.payment_processing_system.vault.repository;

import com.sohamtanpathak.fintech.payment_processing_system.vault.entity.VaultCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaultCardRepository extends JpaRepository<VaultCard, UUID> {
}
