package com.bugalin.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public class ConfigJson {
    private SshManagerData sshManagerData;
    private FileHandlerData fileHandlerData;

    public ConfigJson(){}

    public SshManagerData getSshManagerData() {
        return sshManagerData;
    }
    public void setSshManagerData(SshManagerData sshManagerData) {
        this.sshManagerData = sshManagerData;
    }
    public FileHandlerData getFileHandlerData() {return fileHandlerData;}
    public void setFileHandlerData(FileHandlerData fileHandlerData) {this.fileHandlerData = fileHandlerData;}

    @JsonIgnore
    public static ConfigJson defaultConfig(){
        ConfigJson configJson = new ConfigJson();
        configJson.setSshManagerData(SshManagerData.getdefault());

        FileHandlerData fileHandlerData = new FileHandlerData();
        fileHandlerData.setCurrentDir("/");
        fileHandlerData.setPathAlias(new HashMap<>());
        configJson.setFileHandlerData(fileHandlerData);

        return configJson;
    }
}
