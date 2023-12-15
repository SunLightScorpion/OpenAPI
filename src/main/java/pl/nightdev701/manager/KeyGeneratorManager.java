package pl.nightdev701.manager;

import pl.nightdev701.crypto.generator.AESKeyGenerator;
import pl.nightdev701.crypto.generator.BlowFishGenerator;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.CryptType;
import pl.nightdev701.util.key.CryptKeyGenerator;

public class KeyGeneratorManager {

    private CryptKeyGenerator generator;

    public KeyGeneratorManager(CryptType type, AbstractLogger logger) {

        if (type == CryptType.AES) {
            this.generator = new AESKeyGenerator(logger);
        }
        if (type == CryptType.BLOWFISH) {
            this.generator = new BlowFishGenerator(logger);
        }
    }

    public String generateKey() {
        return generator.generateKey();
    }

}
