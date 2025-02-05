package pl.nightdev701.io;

/*

Lukas - 15:34
26.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * string key
 */
public class ValueKey<T> {

    private static final String BASE_PATH = "key_values";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String namespace;
    private final String key;
    AbstractLogger logger;

    /**
     *
     * Constructor from value key class
     *
     */
    private ValueKey(String namespace, String key, AbstractLogger logger) {
        this.namespace = namespace;
        this.key = key;
        this.logger = logger;

        File file = new File("key_values");

        if (!file.exists()) {
            if(file.mkdir()){
                logger.log("INFO", "Create data folder for value keys");
            }
        }
    }

    /**
     * create key
     *
     * @return key value
     */
    public static <T> ValueKey<T> create(String namespace, String key, AbstractLogger logger) {
        return new ValueKey<>(namespace, key, logger);
    }

    /**
     * create key
     *
     * @return key value
     */
    public static <T> ValueKey<T> create(String namespace, String key) {
        return new ValueKey<>(namespace, key, new DefaultLogger());
    }

    /**
     * read value
     *
     * @return key value
     */
    public T getKeyValue(Class<T> type) {
        try {
            File namespaceFile = new File(BASE_PATH, namespace + ".json");
            if (!namespaceFile.exists()) {
                return null;
            }

            Map<String, Object> data = loadNamespaceData(namespaceFile);
            Object value = data.get(key);
            if (value != null) {
                return GSON.fromJson(GSON.toJson(value), type);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * write value
     */
    public void setKeyValue(T value) {
        try {
            File namespaceFile = new File(BASE_PATH, namespace + ".json");
            Map<String, Object> data = new HashMap<>();

            if (namespaceFile.exists()) {
                data = loadNamespaceData(namespaceFile);
            }

            data.put(key, value);

            saveNamespaceData(namespaceFile, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKey() {
        try {
            File namespaceFile = new File(BASE_PATH, namespace + ".json");
            if (!namespaceFile.exists()) {
                System.out.println("Namespace-Datei existiert nicht.");
                return;
            }

            Map<String, Object> data = loadNamespaceData(namespaceFile);

            if (data.containsKey(key)) {
                data.remove(key);
                System.out.println("Key '" + key + "' wurde gelöscht.");
            } else {
                System.out.println("Key '" + key + "' wurde nicht gefunden.");
                return;
            }

            saveNamespaceData(namespaceFile, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> loadNamespaceData(File file) {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            return GSON.fromJson(reader, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private void saveNamespaceData(File file, Map<String, Object> data) {
        try (Writer writer = Files.newBufferedWriter(file.toPath())) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
