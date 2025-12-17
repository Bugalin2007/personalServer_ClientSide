package com.bugalin.handler;

import com.bugalin.data.ExecResult;
import com.bugalin.data.FileHandlerData;

import java.util.Map;

public class RemoteFileHandler {
    private final SSHManager sshManager;
    private String currentDir;
    private Map<String,String> pathAlias;

    public RemoteFileHandler(FileHandlerData fileHandlerData, SSHManager sshManager) {
        this.sshManager = sshManager;
        this.currentDir = fileHandlerData.getCurrentDir();
        this.pathAlias = fileHandlerData.getPathAlias();
    }


    private StruDirectory getDirectoryContent(String path){
        ExecResult received = sshManager.executeCommandAtDir("ls -a",path);
        if(!received.isSuccess()){return null;}
        String name = path.substring(path.lastIndexOf("/")+1);
        String[] fileNames = received.output().split("\n");
        StruFile[] files = new StruFile[fileNames.length];
        for(int i = 0; i < fileNames.length; i++){
            files[i] = new StruFile(fileNames[i],path+"/"+fileNames[i]);
        }
        return new StruDirectory(name,path,files);
    }
    private static class StruDirectory{
        private String name;
        private String path;
        private StruFile[] files;

        public StruDirectory(String name, String path, StruFile[] files) {
            this.name = name;
            this.path = path;
            this.files = files;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public StruFile[] getFiles() {
            return files;
        }

        public void setFiles(StruFile[] files) {
            this.files = files;
        }
    }

    private static class StruFile{
        private String name;
        private String path;

        public StruFile(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
