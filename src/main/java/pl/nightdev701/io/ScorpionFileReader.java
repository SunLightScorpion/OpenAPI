package pl.nightdev701.io;

import pl.nightdev701.logger.AbstractLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class ScorpionFileReader {

    private final AbstractLogger logger;
    private BufferedReader bufferedReader;
    private String currentLine;

    public ScorpionFileReader(String filePath, AbstractLogger logger) {

        this.logger = logger;

        try {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            currentLine = bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "The file was not found: " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while reading the file: " + e.getMessage());
        }
    }

    /**
     * get current line
     */
    public String getCurrentLine() {
        return currentLine;
    }

    /**
     * read next line
     */
    public void readNextLine() {
        try {
            currentLine = bufferedReader.readLine();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while reading the next line: " + e.getMessage());
        }
    }

    /**
     * close io stream
     */
    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while closing the reader: " + e.getMessage());
        }
    }

}
