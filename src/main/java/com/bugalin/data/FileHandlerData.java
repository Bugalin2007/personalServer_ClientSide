package com.bugalin.data;

import java.util.Map;

public class FileHandlerData {
    private String currentDir;
    private Map<String,String> pathAlias;

    public FileHandlerData() {}

    public String getCurrentDir() {return currentDir;}
    public void setCurrentDir(String currentDir) {this.currentDir = currentDir;}
    public Map<String, String> getPathAlias() {return pathAlias;}
    public void setPathAlias(Map<String, String> pathAlias) {this.pathAlias = pathAlias;}

}
