package pl.nightdev701.crypto.generator;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.key.CryptKeyGenerator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class BlowFishGenerator implements CryptKeyGenerator {

    private AbstractLogger logger;

    public BlowFishGenerator(AbstractLogger logger) {
        this.logger = logger;
    }

    @Override
    public String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");

            keyGenerator.init(128);

            SecretKey secretKey = keyGenerator.generateKey();

            byte[] keyBytes = secretKey.getEncoded();

            logger.log(Level.INFO, "Key generated for algorithms: Blowfish");

            return java.util.Base64.getEncoder().encodeToString(keyBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.log(Level.INFO, "Key failed to generate for algorithms: AES");
        }
        return null;
    }

}
