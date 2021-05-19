package com.example.demo.Bot.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSHA1Handler {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Gets byte encrypted data encrypted by using HmacSHA1 encryption from byte data and byte key.
     * @param data Data to be encrypted
     * @param key Encryption Key
     * @return Encrypted data
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] calculateHMACSHA1(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data);
    }
}
