package com.ivan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for managing application properties.
 * This class provides methods for loading and accessing properties from the "application.properties" file.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {

    /**
     * The properties loaded from the "application.properties" file.
     */
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Loads properties from the "application.properties" file into the {@link Properties} object.
     * This method is called automatically when the class is loaded.
     * If the file cannot be found or loaded, a {@link RuntimeException} is thrown.
     */
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the value associated with the specified key from the loaded properties.
     *
     * @param key the key whose associated value is to be retrieved
     * @return the value associated with the specified key, or {@code null} if the key is not found
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}