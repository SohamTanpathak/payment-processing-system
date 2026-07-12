package com.sohamtanpathak.fintech.payment_processing_system.vault.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class VaultEncryptionConfig {

    /**
     * Configure vault encryption, like how we want to encrypt our dek and pan as well
     */

    @Value("${vault.master-key}")
    private String masterKey;

    /**
     * It is used to encrypt the pan using dek.
     * It will return an encrypter using the dek and using that encrypter the pan can be encrypted.
     */
    public static BytesEncryptor panEncryptor(byte[] dek){

        SecretKeySpec decKey = new SecretKeySpec(dek, "AES");
        return new AesBytesEncryptor(decKey, KeyGenerators.secureRandom(12),
                AesBytesEncryptor.CipherAlgorithm.GCM);

    }


    /**
     * It is used to encrypt the dek using masterKey.
     * Every dek will be encrypted on the basis of this master key, so it can be a
     * standard value across the application, hence it's a Bean
     */
    @Bean
    public BytesEncryptor dekEncryptor(){

        byte[] masterKeyBytes = Base64.getDecoder().decode(masterKey);
        SecretKeySpec masterDecKey = new SecretKeySpec(masterKeyBytes, "AES");
        return new AesBytesEncryptor(masterDecKey, KeyGenerators.secureRandom(12),
                AesBytesEncryptor.CipherAlgorithm.GCM);
    }
}
