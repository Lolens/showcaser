package io.github.lolens.showcaser.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import io.github.lolens.showcaser.Showcaser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_FOLDER = Platform.getConfigFolder().resolve(Showcaser.MOD_ID);

    private static final Path CONFIG = CONFIG_FOLDER.resolve(Showcaser.MOD_ID + "-config.json");
    private static final Path PERSISTENT_STORAGE = CONFIG_FOLDER.resolve(Showcaser.MOD_ID + "-storage.json");


    private static ShowcaserConfig config;
    private static ShowcaserStorage storage;

    public static ShowcaserConfig getConfig() {
        if (config == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return config;
    }

    public static ShowcaserStorage getStorage() {
        if (storage == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return storage;
    }

    private static <T> T load(Path path, Class<T> type, T defaultValue) {
        try {
            Files.createDirectories(path.getParent());

            if (Files.notExists(path)) {
                save(path, defaultValue);
                return defaultValue;
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                T obj = GSON.fromJson(reader, type);
                return obj != null ? obj : defaultValue;
            }

        } catch (IOException e) {
            Showcaser.LOGGER.error("Failed to load file. Path: " + path, e);
            return defaultValue;
        }
    }

    // must be called after loading
    private static <T> void save(Path path, T value) {
        try {
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                GSON.toJson(value, writer);
            }

        } catch (IOException e) {
            Showcaser.LOGGER.error("Failed to save file. Path: " + path, e);
        }
    }

    public static void saveStorage() {
        save(PERSISTENT_STORAGE, storage);
    }

    public static void saveConfig() {
        save(CONFIG, config);
    }

    public static void loadAll() {
        Showcaser.LOGGER.info("Loaded configs");
        config = load(CONFIG, ShowcaserConfig.class, new ShowcaserConfig());
        storage = load(PERSISTENT_STORAGE, ShowcaserStorage.class, new ShowcaserStorage());
    }

    public static void saveAll() {
        save(CONFIG, config);
        save(PERSISTENT_STORAGE, storage);
    }

}
