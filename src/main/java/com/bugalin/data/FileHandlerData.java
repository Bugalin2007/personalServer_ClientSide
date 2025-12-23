package com.bugalin.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class FileHandlerData {


    private String currentDirPath;
    private String currentDirName;
    private int browseHistorySize;
    private Map<String,String> pathAlias;

    public FileHandlerData() {}

    @JsonIgnore
    public static FileHandlerData getdefault() {
        FileHandlerData fileHandlerData = new FileHandlerData();
        fileHandlerData.setCurrentDirPath("/");
        fileHandlerData.setCurrentDirName("");
        fileHandlerData.setBrowseHistorySize(10);
        fileHandlerData.setPathAlias(new HashMap<>());
        return fileHandlerData;
    }

    public String getCurrentDirPath() {return currentDirPath;}
    public void setCurrentDirPath(String currentDirPath) {this.currentDirPath = currentDirPath;}
    public String getCurrentDirName() {return currentDirName;}
    public void setCurrentDirName(String currentDirName) {this.currentDirName = currentDirName;}
    public int getBrowseHistorySize() {return browseHistorySize;}
    public void setBrowseHistorySize(int browseHistorySize) {this.browseHistorySize = browseHistorySize;}
    public Map<String, String> getPathAlias() {return pathAlias;}
    public void setPathAlias(Map<String, String> pathAlias) {this.pathAlias = pathAlias;}

}
