package pl.nightdev701.crypto.generator;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.key.CryptKeyGenerator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class AESKeyGenerator implements CryptKeyGenerator {

    AbstractLogger logger;

    public AESKeyGenerator(AbstractLogger logger) {
        this.logger = logger;
    }

    /**
     * Generate Key
     */
    @Override
    public String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

            keyGenerator.init(128);

            SecretKey secretKey = keyGenerator.generateKey();

            byte[] keyBytes = secretKey.getEncoded();

            logger.log(Level.INFO, "Key generated for algorithms: AES");

            return java.util.Base64.getEncoder().encodeToString(keyBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Key failed to generate for algorithms: AES");
        }
        return null;
    }

}