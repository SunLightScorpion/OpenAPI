package pl.nightdev701.io;

import pl.nightdev701.logger.AbstractLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class ScorpionFileReader {

    private BufferedReader bufferedReader;
    private String currentLine;
    private AbstractLogger logger;

    public ScorpionFileReader(String filePath, AbstractLogger logger) {

        this.logger = logger;

        try {
            FileReader fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            currentLine = bufferedReader.readLine(); // Lese die erste Zeile
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING,"Die Datei wurde nicht gefunden: " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING,"Fehler beim Lesen der Datei: " + e.getMessage());
        }
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public void readNextLine() {
        try {
            currentLine = bufferedReader.readLine(); // Gehe zur nächsten Zeile
        } catch (IOException e) {
            logger.log(Level.WARNING,"Fehler beim Lesen der nächsten Zeile: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"Fehler beim Schließen des Readers: " + e.getMessage());
        }
    }

}
