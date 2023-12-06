package pl.nightdev701.util.crypto;

public interface CryptoForm {

    String encrypt(String plainText) throws Exception;

    String decrypt(String encryptedText) throws Exception;

}
