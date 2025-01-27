package pl.nightdev701.util.crypto;

import java.io.File;

public interface FileCryptoForm {

    /**
     * encrypt file
     */
    void encrypt(String inputFile, String outputFile) throws Exception;

    /**
     * decrypt file
     */
    void decrypt(String inputFile, String outputFile) throws Exception;

}
