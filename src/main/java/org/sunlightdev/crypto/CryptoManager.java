package org.sunlightdev.crypto;

/*

Lukas - 21:45
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoManager {

    String ENCRYPTION_KEY;

    public CryptoManager(String key){
        this.ENCRYPTION_KEY = key;
    }

    private String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return new String(encryptedBytes);
    }

    private String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedText.getBytes());
        return new String(decryptedBytes);
    }

}
