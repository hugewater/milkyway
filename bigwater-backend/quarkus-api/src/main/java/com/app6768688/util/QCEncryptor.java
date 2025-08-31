package com.app6768688.util;

import jakarta.enterprise.context.ApplicationScoped;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class QCEncryptor 
{
    private static final String PUBLIC_KEY_PEM =
            "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3m60wZMcl+Ms1IBRoHl2PWQfOUjmZTsI4QHhGKXuS4YDYIkd8Q7IofmPhPC1SFdOqMI+d3A2ifN/QLjWLNfvML459z0MQs/B8JkO+i/CAZvXUoFT+pSJcK7mscDbX1JDPyOuMWvBn1BH/1jppPhL943YIfPx9gWfnZHLreeU3ubOdWLeeZmlwZ50WScFQGwHIs93XH7Ekzz/09SIPF2Oizmsahx4QwT+kayDg+scVX9SnNEHymBE3azzqiQ6AUkyUcthkyDmIy7FeVJcoL481JBjRm0pDBWazvGk37p4ABdkskNaBkdulrskERwjrz3TIjerus1yQy+RA5AGNHCFjQIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    // --- Algorithms ---
    private static final String RSA_TRANSFORM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final OAEPParameterSpec OAEP_SHA256 =
            new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);

    private static final String AES_TRANSFORM = "AES/GCM/NoPadding";
    private static final int AES_KEY_BITS = 256;
    private static final int GCM_TAG_BITS = 128;
    private static final int GCM_IV_BYTES = 12;

    // --- FIXED seed for deterministic testing (CHANGE for your tests) ---
    private static final byte[] FIXED_SEED = new byte[] {
        0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,
        0x10,0x20,0x30,0x40,0x50,0x60,0x70,0x7F
    };

    /** hybrid encryption returning [ encKeyB64, ivB64, ctB64 ]. */
    public static String encrypt(String message) throws Exception {
        // 1) Build a fixed-seeded SecureRandom
        // Option A: DRBG/SHA1PRNG may vary by platform; the key is setting a known seed.
        PublicKey pub = loadPublicKeyFromConst(PUBLIC_KEY_PEM);
        
        SecureRandom fixedRnd = SecureRandom.getInstance("SHA1PRNG");
        fixedRnd.setSeed(FIXED_SEED);

        // 2) AES key
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(AES_KEY_BITS, fixedRnd);
        SecretKey aesKey = kg.generateKey();

        // 3) IV (12 bytes for GCM)
        byte[] iv = new byte[GCM_IV_BYTES];
        fixedRnd.nextBytes(iv);

        // 4) AES-GCM encrypt (deterministic because key+iv are deterministic)
        Cipher aes = Cipher.getInstance(AES_TRANSFORM);
        aes.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
        byte[] ct = aes.doFinal(message.getBytes());

        // 5) RSA-OAEP encrypt AES key (pass fixedRnd so OAEP's internal randomness is deterministic)
        Cipher rsa = Cipher.getInstance(RSA_TRANSFORM);
        rsa.init(Cipher.ENCRYPT_MODE, pub, OAEP_SHA256, fixedRnd);
        byte[] encKey = rsa.doFinal(aesKey.getEncoded());

        return b64(ct);
    }
     private static PublicKey loadPublicKeyFromConst(String pem) throws Exception {
        String b64 = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(b64);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
    }
    private static PublicKey loadPublicKeyPEM(String pem) throws Exception {
        String b64 = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(b64);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
    }

    private static String b64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }



}