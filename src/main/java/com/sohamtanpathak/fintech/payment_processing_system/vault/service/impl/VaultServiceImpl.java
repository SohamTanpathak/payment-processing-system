package com.sohamtanpathak.fintech.payment_processing_system.vault.service.impl;

import com.sohamtanpathak.fintech.payment_processing_system.common.entity.Money;
import com.sohamtanpathak.fintech.payment_processing_system.common.enums.CardBrand;
import com.sohamtanpathak.fintech.payment_processing_system.common.exceptions.ResourceNotFoundException;
import com.sohamtanpathak.fintech.payment_processing_system.common.util.RandomizerUtil;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.PaymentProcessorRouter;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorRequest;
import com.sohamtanpathak.fintech.payment_processing_system.payment.processor.dto.PaymentProcessorResponse;
import com.sohamtanpathak.fintech.payment_processing_system.vault.config.VaultEncryptionConfig;
import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.request.TokenizeRequest;
import com.sohamtanpathak.fintech.payment_processing_system.vault.dto.response.TokenizeResponse;
import com.sohamtanpathak.fintech.payment_processing_system.vault.entity.CardToken;
import com.sohamtanpathak.fintech.payment_processing_system.vault.entity.VaultCard;
import com.sohamtanpathak.fintech.payment_processing_system.vault.repository.CardTokenRepository;
import com.sohamtanpathak.fintech.payment_processing_system.vault.repository.VaultCardRepository;
import com.sohamtanpathak.fintech.payment_processing_system.vault.service.VaultService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final VaultCardRepository vaultCardRepository;
    private final CardTokenRepository cardTokenRepository;
    private final BytesEncryptor dekEncrypter;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0,6);
        CardBrand cardBrand = detectBrand(request.pan());

        // secret key for pan encryption
        byte[] dek = KeyGenerators.secureRandom(32).generateKey();


        // pan encryption
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));

        // dek encryption
        byte[] encryptedDek = dekEncrypter.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .brand(cardBrand)
                .expiryYear(request.expiryYear().toString())
                .expiryMonth(request.expiryMonth().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .cardHolderName(request.cardHolderName())
                .build();

        vaultCardRepository.save(vaultCard);

        String token = "tok_"+ RandomizerUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .vaultCard(vaultCard)
                .token(token)
                .customer(request.customerId())
                .merchant(merchantId)
                .build()
        );



        return new TokenizeResponse(token, lastFour, cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {

        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        // get the actual vault card from the cardToken, this vault card has encrypted pan which needs to be decrypted
        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

        try {
            // for decrypting the pan, firstly need to decrypt the dek
            byte[] dek = dekEncrypter.decrypt(vaultCard.getEncryptedDek()); //decrypt the dek
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan()); //decrypt the pan

            String pan = new String(panBytes, StandardCharsets.UTF_8); // get the pan in String (from byte[])

            String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest
                    .card(paymentId, pan, expiry, amount, methodDetails);

            PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token = {}****", token.substring(0, 4));

            return response;
        } catch (Exception e) {
            log.warn("Vault charge failed, token= {}****", token.substring(0,4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", e.getMessage());
        } finally {
            if(panBytes != null)    Arrays.fill(panBytes, (byte) 0);  // making panBytes null just to be safe
        }
    }

    private CardBrand detectBrand(String pan){

        if(pan.startsWith("4")) return CardBrand.VISA;
        if(pan.startsWith("5") || pan.startsWith("2"))  return CardBrand.MASTERCARD;
        if(pan.startsWith("37") || pan.startsWith("34"))  return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
