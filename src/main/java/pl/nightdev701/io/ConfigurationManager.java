package pl.nightdev701.io;

import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigurationManager {

    private final Map<String, String> dataMap;
    private final String path;
    private final AbstractLogger logger;
    private final boolean allowLogging;

    public ConfigurationManager(String path, AbstractLogger logger, boolean allowLogging) {
        this.logger = logger;
        this.allowLogging = allowLogging;
        this.path = path.endsWith(".slsd") ? path : path + ".slsd";
        this.dataMap = new LinkedHashMap<>();
        readConfig();
    }

    /**
     * Read config file into dataMap
     */
    private void readConfig() {
        synchronized (this) {
            File file = new File(path);

            if (!file.exists()) {
                if (allowLogging) logger.log(Level.INFO, "Config file not found, will be created when needed.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                dataMap.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        dataMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
                if (allowLogging) logger.log(Level.INFO, "Config read successfully.");
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to read config: " + e.getMessage());
            }
        }
    }

    /**
     * Get value
     */
    public synchronized String getValue(String key) {
        return dataMap.get(key);
    }

    /**
     * Check if key exists
     */
    public synchronized boolean containsKey(String key) {
        return dataMap.containsKey(key);
    }

    /**
     * Check if file exists
     */
    public boolean existFile() {
        return new File(path).exists();
    }

    /**
     * Check if key is set
     */
    public synchronized boolean isSet(String key) {
        return dataMap.containsKey(key);
    }

    /**
     * Remove a key from config
     */
    public void remove(String key) {
        synchronized (this) {
            if (!isSet(key)) {
                if (allowLogging) logger.log(Level.WARNING, "Key does not exist: " + key);
                return;
            }
            writeWithLock((writer) -> {
                dataMap.remove(key);
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
            });
            readConfig();
        }
    }

    /**
     * Add a new key-value
     */
    public void add(String key, String value) {
        synchronized (this) {
            if (isSet(key)) {
                if (allowLogging) logger.log(Level.WARNING, "Key already exists: " + key);
                return;
            }
            dataMap.put(key, value);
            writeWithLock((writer) -> {
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
            });
            readConfig();
        }
    }

    /**
     * Set value (update existing or add new)
     */
    public void set(String key, String value) {
        synchronized (this) {
            dataMap.put(key, value);
            writeWithLock((writer) -> {
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
            });
            readConfig();
        }
    }

    /**
     * Core method to write the dataMap to disk with exclusive FileLock and temp file
     */
    private void writeWithLock(FileWriterAction action) {
        File file = new File(path);
        File tempFile = new File(path + ".tmp");

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock();
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            action.write(writer);
            writer.flush();

            if (!tempFile.renameTo(file)) {
                if (allowLogging) logger.log(Level.WARNING, "Failed to rename temp file to original file");
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, "Error writing config with FileLock: " + e.getMessage());
        }
    }

    /**
     * Functional interface for writing action
     */
    @FunctionalInterface
    private interface FileWriterAction {
        void write(BufferedWriter writer) throws IOException;
    }

}
