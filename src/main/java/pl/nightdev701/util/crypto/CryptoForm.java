package pl.nightdev701.util.crypto;

public interface CryptoForm {

    /**
     * encrypt value
     */
    String encrypt(String plainText) throws Exception;

    /**
     * decrypt value
     */
    String decrypt(String encryptedText) throws Exception;

}
