package com.bugalin.command.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class RemoteFileHandlerData {
    private String currentDir;
    private Map<String,String> pathAlias;

    public RemoteFileHandlerData() {}

    public String getCurrentDir() {return currentDir;}
    public void setCurrentDir(String currentDir) {this.currentDir = currentDir;}
    public Map<String, String> getPathAlias() {return pathAlias;}
    public void setPathAlias(Map<String, String> pathAlias) {this.pathAlias = pathAlias;}

}
