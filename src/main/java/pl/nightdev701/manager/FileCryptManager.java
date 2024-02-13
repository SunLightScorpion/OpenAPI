package pl.nightdev701.manager;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.security.file.AesFileSecurity;
import pl.nightdev701.util.CryptType;
import pl.nightdev701.util.crypto.FileCryptoForm;

public class FileCryptManager {

    private FileCryptoForm crypt;

    public FileCryptManager(String key, CryptType type, AbstractLogger logger) {
        if (type == CryptType.AES) {
            this.crypt = new AesFileSecurity(key, logger);
        }
        /*if (type == CryptType.BLOWFISH) {
            this.crypt = new BlowFishCrypto(key, logger);
        }*/
    }

    public void encrypt(String inputFile, String outputFile) {
        try {
            getCrypt().encrypt(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decrypt(String inputFile, String outputFile) {
        try {
            getCrypt().decrypt(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileCryptoForm getCrypt() {
        return crypt;
    }

}
