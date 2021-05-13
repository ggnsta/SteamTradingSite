package com.example.demo.Bot.Utils;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class RSAEncrypt {

    private final String PASSWORD;
    private final String publickey_mod;
    private final String publickey_exp;

    public RSAEncrypt(String pass, String publickey_mod, String publickey_exp){
        this.PASSWORD = pass;
        this.publickey_exp = publickey_exp;
        this.publickey_mod = publickey_mod;
    }

    // encrypt the password
    private String encrypt(){

        BigInteger mod = new BigInteger(publickey_mod, 16);
        BigInteger exp = new BigInteger(publickey_exp, 16);
        String encryptedPassword = null;

        try {
            RSAPublicKeySpec spec = new RSAPublicKeySpec(mod, exp);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(spec);
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherText = rsa.doFinal(PASSWORD.getBytes());

            encryptedPassword = Base64.getEncoder().encodeToString(cipherText);

        } catch (Exception e) {
            System.out.println("There was a problem encrypting Password.");
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    public String getEncryptedPassword(){
        return encrypt();
    }
}