package pl.nightdev701.manager;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.security.generator.AESKeyGenerator;
import pl.nightdev701.security.generator.BlowFishGenerator;
import pl.nightdev701.util.CryptType;
import pl.nightdev701.util.key.CryptKeyGenerator;

public class KeyGeneratorManager {

    private CryptKeyGenerator generator;
    private boolean print;

    public KeyGeneratorManager(CryptType type, AbstractLogger logger) {
        if (type == CryptType.AES) {
            this.generator = new AESKeyGenerator(logger);
        }
        if (type == CryptType.BLOWFISH) {
            this.generator = new BlowFishGenerator(logger);
        }
    }

    /**
     * toggle print
     */
    public KeyGeneratorManager setPrint(boolean print) {
        this.print = print;
        return this;
    }

    /**
     * get generated key
     */
    public String generateKey() {
        String key = generator.generateKey();

        if (print) {
            System.out.println("Crypt Key: " + key);
        }
        return key;
    }

}
