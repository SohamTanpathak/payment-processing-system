package com.sohamtanpathak.fintech.payment_processing_system.merchant.repository;

import com.sohamtanpathak.fintech.payment_processing_system.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    boolean existsByEmail(String email);
}
