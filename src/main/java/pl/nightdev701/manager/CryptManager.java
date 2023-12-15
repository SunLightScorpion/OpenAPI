package pl.nightdev701.manager;

import pl.nightdev701.crypto.BlowFishCrypto;
import pl.nightdev701.crypto.CryptoAes;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.CryptType;
import pl.nightdev701.util.crypto.CryptoForm;

public class CryptManager {

    private CryptoForm crypt;

    public CryptManager(String key, CryptType type, AbstractLogger logger) {
        if (type == CryptType.AES) {
            this.crypt = new CryptoAes(key, logger);
        }
        if (type == CryptType.BLOWFISH) {
            this.crypt = new BlowFishCrypto(key, logger);
        }
    }

    public String encrypt(String text) {
        try {
            return getCrypt().encrypt(text);
        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String text) {
        try {
            return getCrypt().decrypt(text);
        } catch (Exception e) {
            return null;
        }
    }

    public CryptoForm getCrypt() {
        return crypt;
    }

}
