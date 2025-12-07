package com.bugalin;

import com.bugalin.command.data.Config;
import com.bugalin.command.data.SshManagerData;
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

    public void readConfig(){
        File file = new File("config.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.jObjectConfig = Config.defaultConfig();
                objectMapper.writeValue(file, this.jObjectConfig);
                return;
            }
            this.jObjectConfig = objectMapper.readValue(file, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(){
        File file = new File("config.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, this.jObjectConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Config getConfig() {
        return this.jObjectConfig;
    }

    public void setSshManagerData(SshManagerData sshManagerData) {
        this.jObjectConfig.setSshManagerData(sshManagerData);
    }
}
