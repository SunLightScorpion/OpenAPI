package pl.nightdev701.security.rsa;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.crypto.CryptoForm;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CryptoRsa implements CryptoForm {

    private final String privateKeyPath;
    private final String publicKeyPath;
    private final AbstractLogger logger;

    public CryptoRsa(String privateKeyPath, String publicKeyPath, AbstractLogger logger) {
        this.privateKeyPath = privateKeyPath;
        this.publicKeyPath = publicKeyPath;
        this.logger = logger;
    }

    /**
     * Lädt den öffentlichen Schlüssel aus der PEM-Datei
     */
    private PublicKey loadPublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        String key = new String(keyBytes);
        key = key.replaceAll("-----.*PUBLIC KEY-----", "").replaceAll("\\s+", ""); // Bereinigung
        byte[] decodedKey = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Lädt den privaten Schlüssel aus der PEM-Datei
     */
    private PrivateKey loadPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        String key = new String(keyBytes);
        key = key.replaceAll("-----.*PRIVATE KEY-----", "").replaceAll("\\s+", ""); // Bereinigung
        byte[] decodedKey = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Encrypt data using RSA (mit Blockaufteilung)
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        PublicKey publicKey = loadPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Splitte den Text in kleine Blöcke
        List<String> blocks = splitIntoBlocks(plainText, 245); // Maximalgröße für RSA bei 2048 Bit
        List<String> encryptedBlocks = encryptBlocks(blocks, cipher);

        // Verbinde verschlüsselte Blöcke
        return String.join(" ", encryptedBlocks);
    }

    /**
     * Decrypt data using RSA (mit Blockverarbeitung)
     */
    @Override
    public String decrypt(String encryptedText) throws Exception {
        PrivateKey privateKey = loadPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Splitte verschlüsselte Textblöcke und entschlüssle sie
        List<String> encryptedBlocks = List.of(encryptedText.split(" "));
        List<String> decryptedBlocks = decryptBlocks(encryptedBlocks, cipher);

        // Verbinde entschlüsselte Blöcke
        return String.join("", decryptedBlocks);
    }

    // Hilfsmethode zum Aufteilen des Klartexts in Blöcke
    private List<String> splitIntoBlocks(String data, int blockSize) {
        List<String> blocks = new ArrayList<>();
        int length = data.length();

        for (int i = 0; i < length; i += blockSize) {
            blocks.add(data.substring(i, Math.min(length, i + blockSize)));
        }
        return blocks;
    }

    // Verschlüsselt eine Liste von Blöcken
    private List<String> encryptBlocks(List<String> blocks, Cipher cipher) throws Exception {
        List<String> encryptedBlocks = new ArrayList<>();
        for (String block : blocks) {
            byte[] encrypted = cipher.doFinal(block.getBytes());
            encryptedBlocks.add(Base64.getEncoder().encodeToString(encrypted));
        }
        return encryptedBlocks;
    }

    // Entschlüsselt eine Liste von verschlüsselten Blöcken
    private List<String> decryptBlocks(List<String> encryptedBlocks, Cipher cipher) throws Exception {
        List<String> decryptedBlocks = new ArrayList<>();
        for (String encryptedBlock : encryptedBlocks) {
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedBlock));
            decryptedBlocks.add(new String(decrypted));
        }
        return decryptedBlocks;
    }

    // Signiert eine Nachricht mit dem privaten Schlüssel
    public String sign(String message) throws Exception {
        PrivateKey privateKey = loadPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedMessage = signature.sign();
        return Base64.getEncoder().encodeToString(signedMessage);
    }

    // Verifiziert die Signatur mit dem öffentlichen Schlüssel
    public boolean verify(String message, String signedMessage) throws Exception {
        PublicKey publicKey = loadPublicKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        byte[] decodedSignedMessage = Base64.getDecoder().decode(signedMessage);
        return signature.verify(decodedSignedMessage);
    }
}
