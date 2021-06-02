package com.example.demo.Bot.Utils;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class RSAEncrypt {

    private final String password;
    private final String publickeyMod;
    private final String publickeyExp;

    public RSAEncrypt(String pass, String publickeyMod, String publickeyExp){
        this.password = pass;
        this.publickeyExp = publickeyExp;
        this.publickeyMod = publickeyMod;
    }

    private String encrypt(){

        BigInteger mod = new BigInteger(publickeyMod, 16);
        BigInteger exp = new BigInteger(publickeyExp, 16);
        String encryptedPassword = null;

        try {
            RSAPublicKeySpec spec = new RSAPublicKeySpec(mod, exp);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(spec);
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherText = rsa.doFinal(password.getBytes());

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