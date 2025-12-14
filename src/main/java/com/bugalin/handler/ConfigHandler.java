package com.bugalin.handler;

import com.bugalin.data.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public SshManagerData getSshManagerData() {
        return sshManagerData;
    }

    public void setSshManagerData(SshManagerData sshManagerData) {
        this.sshManagerData = sshManagerData;
    }

    public FileHandlerData getFileHandlerData() {
        return fileHandlerData;
    }

    public void setFileHandlerData(FileHandlerData fileHandlerData) {
        this.fileHandlerData = fileHandlerData;
    }

    private SshManagerData sshManagerData;
    private FileHandlerData fileHandlerData;

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void readConfig(){
        File file = new File("configJson.json");
        ConfigJson json;
        try {
            if (!file.exists()) {
                file.createNewFile();
                json = ConfigJson.defaultConfig();
                objectMapper.writeValue(file, json);
                return;
            }
            json = objectMapper.readValue(file, ConfigJson.class);
            this.sshManagerData = json.getSshManagerData();
            this.fileHandlerData = json.getFileHandlerData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(){
        File file = new File("configJson.json");
        ConfigJson json = new ConfigJson();
        json.setSshManagerData(sshManagerData);
        json.setFileHandlerData(fileHandlerData);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
