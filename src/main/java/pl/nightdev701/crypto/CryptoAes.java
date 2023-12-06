package pl.nightdev701.crypto;

/*

Lukas - 21:45
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;
import pl.nightdev701.util.crypto.CryptoForm;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;

public class CryptoAes implements CryptoForm {

    private final String encryptionKey;
    private final AbstractLogger logger;

    public CryptoAes(String encryptionKey) {
        this(encryptionKey, new DefaultLogger());
    }

    public CryptoAes(String encryptionKey, AbstractLogger logger) {
        this.encryptionKey = encryptionKey;
        this.logger = logger;
    }

    /**
     * encrypt data
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(this.encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        logger.log(Level.INFO, "String encrypted");

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * decrypt data
     */
    @Override
    public String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(this.encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        logger.log(Level.INFO, "String decrypted");

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
