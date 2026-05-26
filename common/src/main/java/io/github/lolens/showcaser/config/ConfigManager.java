/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.event.client.ClientConfigLoadEvent;
import io.github.lolens.showcaser.api.event.client.ClientConfigSyncEvent;
import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

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

    private static final Path SERVER_CONFIG = CONFIG_FOLDER.resolve(Showcaser.MOD_ID + "-server-config.json");
    private static final Path CLIENT_CONFIG = CONFIG_FOLDER.resolve(Showcaser.MOD_ID + "-client-config.json");
    private static final Path SERVER_PERSISTENT_STORAGE = CONFIG_FOLDER.resolve(Showcaser.MOD_ID + "-server-storage.json");


    private static ShowcaserClientConfig clientConfig;
    private static ShowcaserServerConfig clientServerSyncedValues;

    private static ShowcaserServerConfig serverConfig;
    private static ShowcaserStorage storage;

    public static ShowcaserServerConfig getServerConfig() {
        if (serverConfig == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return serverConfig;
    }

    public static ShowcaserClientConfig getClientConfig() {
        if (clientConfig == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return clientConfig;
    }

    public static ShowcaserServerConfig getSyncedConfig() {
        if (clientServerSyncedValues == null) {
            throw new IllegalStateException("Config not loaded");
        }
        return clientServerSyncedValues;
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
                Showcaser.LOGGER.info("No config found. Creating new one!");
                save(path, defaultValue);
                return defaultValue;
            }

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                int fileVersion = json.has("version") ? json.get("version").getAsInt() : 0;

                if (fileVersion < getCurrentVersion(type)) {
                    Showcaser.LOGGER.info("Outdated config on path {}. Overwriting with default config", path);
                    save(path, defaultValue);
                    return defaultValue;
                }

                T obj = GSON.fromJson(json, type);
                return obj != null ? obj : defaultValue;
            }

        } catch (IOException e) {
            Showcaser.LOGGER.error("Failed to load file. Path: {}", path, e);
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
            Showcaser.LOGGER.error("Failed to save file. Path: {}", path, e);
        }
    }

    private static int getCurrentVersion(Class<?> configClass) {
        try {
            return configClass.getField("CURRENT_VERSION").getInt(null);
        } catch (NoSuchFieldException e) {
            return 0;
        } catch (IllegalAccessException e) {
            Showcaser.LOGGER.error("IllegalAccessException while getting current config version", e);
            return 0;
        }
    }

    public static void saveStorage() {
        save(SERVER_PERSISTENT_STORAGE, storage);
    }
    public static void saveServerConfig() {
        save(SERVER_CONFIG, serverConfig);
    }
    public static void saveClientConfig() {
        save(CLIENT_CONFIG, clientConfig);
    }

    @Environment(EnvType.CLIENT)
    public static void applySyncMessage(ConfigSyncMessage configSyncMessage) {
        if (Platform.getEnvironment() != Env.CLIENT) throw new IllegalStateException("Sync config not on the client thread");
        clientServerSyncedValues = new ShowcaserServerConfig(configSyncMessage);
        ClientConfigSyncEvent.EVENT.invoker().onConfigSync(clientServerSyncedValues);
    }

    public static void syncServerConfigToPlayer(ServerPlayerEntity player) {
        new ConfigSyncMessage(serverConfig).sendTo(player);
    }

    public static void syncServerConfigToAll() {
        MinecraftServer server = GameInstance.getServer();

        for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
            syncServerConfigToPlayer(serverPlayerEntity);
        }
    }

    // client config is reloaded upon rejoining the world
    @Environment(EnvType.CLIENT)
    public static void loadClient() {
        if (Platform.getEnvironment() != Env.CLIENT) throw new IllegalStateException("Load client configs called not on the server thread");
        clientConfig = load(CLIENT_CONFIG, ShowcaserClientConfig.class, new ShowcaserClientConfig());
        Showcaser.LOGGER.info("Loaded client config");
        ClientConfigLoadEvent.EVENT.invoker().onLoad(clientConfig);
    }

    // server config updates on /showcaser reload command or restarting the server
    public static void loadServer() {
        serverConfig = load(SERVER_CONFIG, ShowcaserServerConfig.class, new ShowcaserServerConfig());
        storage = load(SERVER_PERSISTENT_STORAGE, ShowcaserStorage.class, new ShowcaserStorage());
        Showcaser.LOGGER.info("Loaded common config and storage");

        syncServerConfigToAll();
    }

}
