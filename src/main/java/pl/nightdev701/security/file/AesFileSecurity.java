package pl.nightdev701.security.file;

import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.crypto.FileCryptoForm;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class AesFileSecurity implements FileCryptoForm {

    private final String encryptionKey;
    private final AbstractLogger logger;

    public AesFileSecurity(String encryptionKey, AbstractLogger logger) {
        this.encryptionKey = encryptionKey;
        this.logger = logger;
    }

    @Override
    public void encrypt(String inputFile, String outputFile) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(this.encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (InputStream inputStream = new FileInputStream(inputFile);
             OutputStream outputStream = new FileOutputStream(outputFile);
             CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }
        }
        var del = new File(inputFile).delete();

        if(del){
            logger.log(Level.INFO, "File encrypted");
        }
    }

    @Override
    public void decrypt(String inputFile, String outputFile) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(this.encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try (InputStream inputStream = new FileInputStream(inputFile);
             CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        var del = new File(inputFile).delete();

        if(del){
            logger.log(Level.INFO, "File decrypted");
        }
    }

    @Override
    public void encrypt(File inputFile, String outputFile) throws Exception {
        encrypt(inputFile.getAbsolutePath(), outputFile);
    }

    @Override
    public void decrypt(File inputFile, String outputFile) throws Exception {
        decrypt(inputFile.getAbsolutePath(), outputFile);
    }

}
