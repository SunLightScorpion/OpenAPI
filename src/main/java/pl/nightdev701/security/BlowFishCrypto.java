package pl.nightdev701.security;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.crypto.CryptoForm;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;

public class BlowFishCrypto implements CryptoForm {

    private final String encryptionKey;
    private final AbstractLogger logger;

    public BlowFishCrypto(String encryptionKey, AbstractLogger logger) {
        this.encryptionKey = encryptionKey;
        this.logger = logger;
    }

    /**
     * encrypt data
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        logger.log(Level.INFO, "String encrypted");

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * decrypt data
     */
    @Override
    public String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        logger.log(Level.INFO, "String decrypted");

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
