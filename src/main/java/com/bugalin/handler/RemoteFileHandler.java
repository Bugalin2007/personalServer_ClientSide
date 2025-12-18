package com.bugalin.handler;

import com.bugalin.data.ExecResult;
import com.bugalin.data.FileHandlerData;
import com.bugalin.data.FileNode;

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

    private FileNode peekContent(FileNode fileNode) {
        if(fileNode.isContentKnown()) {
            return null;
        }
        ExecResult returnStuff = sshManager.executeCommandAtDir("ls -F",fileNode.getPath());
        if(!returnStuff.isSuccess()) {
            return null;
        }
        String[] AnnotatedFileList = returnStuff.output().split("\n");
        if(AnnotatedFileList.length == 0) {
            return fileNode;
        }
        FileNode[] children = new FileNode[AnnotatedFileList.length];
        String path = fileNode.getPath();
        for(int i = 0; i < children.length; i++) {
            char lastChar = AnnotatedFileList[i].charAt(AnnotatedFileList[i].length()-1);
            String name = AnnotatedFileList[i].substring(0,AnnotatedFileList[i].length()-1);
            switch (lastChar) {
                case '/' ->
                    children[i] = new FileNode(path + '/' + name, name, true);
                case '*' ->
                    children[i] = new FileNode(path + '/' + name, name, FileNode.FileType.EXECUTABLE);
                case '@' ->
                    children[i] = new FileNode(path + '/' + name, name, FileNode.FileType.LINK);
                default -> {
                    name = AnnotatedFileList[i];
                    children[i] = new FileNode(path + '/' + name, name, FileNode.FileType.NORMAL);
                }
            }
        }
        return fileNode.setContent(children);
    }
}
