package com.bugalin;

import com.bugalin.data.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Config jObjectConfig;

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void readConfig() throws IOException {
        File file = new File("config.json");
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("[ERROR] Config file not found during loading, creating default config file.");
            this.jObjectConfig = Config.defaultConfig();
            objectMapper.writeValue(file, this.jObjectConfig);
            return;
        }
        this.jObjectConfig = objectMapper.readValue(file, Config.class);
        System.out.println("[Config] Config file loaded successfully.");
    }

    public void saveConfig() throws IOException {
        File file = new File("config.json");
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("[ERROR] Config file not found during saving, creating config file.");
        }
        objectMapper.writeValue(file, this.jObjectConfig);
        System.out.println("[Config] Config file saved successfully.");
    }

    public Config getConfig() {
        return this.jObjectConfig;
    }

    public void setConfig(Config jObjectConfig) {
        this.jObjectConfig = jObjectConfig;
    }
}
