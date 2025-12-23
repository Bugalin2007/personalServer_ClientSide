package com.bugalin.handler;

import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.data.FileHandlerData;
import com.bugalin.data.FileNode;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Deque;

public class RemoteFileHandler {
    private final SSHManager sshManager;
    private FileNode currentDir;
    private Map<String,String> pathAlias;
    private Deque<FileNode> browseHistory;
    private int browseHistorySize;

    public String getUploadPath() {
        return uploadPath;
    }
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
    public String getDownloadPath() {
        return downloadPath;
    }
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    private String uploadPath;
    private String downloadPath;

    public RemoteFileHandler(FileHandlerData fileHandlerData, SSHManager sshManager) {
        this.sshManager = sshManager;
        this.pathAlias = fileHandlerData.getPathAlias();
        currentDir = new FileNode(fileHandlerData.getCurrentDirPath(), fileHandlerData.getCurrentDirName(), true);
        currentDir = peekContent(currentDir);
        browseHistorySize = fileHandlerData.getBrowseHistorySize();
        browseHistory = new ArrayDeque<>(browseHistorySize);
        uploadPath = fileHandlerData.getUploadPath();
        downloadPath = fileHandlerData.getDownloadPath();
    }

    public String display(){
        return currentDir.toTreeString();
    }

    public String getCurrentDirName() {
        return currentDir.getFileName();
    }

    public String getCurrentDirPath(){
        return currentDir.getPath();
    }

    public ExecResult seeLast(){
        return shiftFocus();
    }

    public ExecResult changeFocusToPath(String path){
        String name = path.split("/")[path.split("/").length-1];
        FileNode newDir = peekContent(new FileNode(path, name, true));
        return shiftFocus(newDir);
    }

    public ExecResult changeFocusToPathInside(String path){
        FileNode focus = currentDir.getFolder(path);
        return shiftFocus(focus);
    }

    public ExecResult openFolder(String path){
        FileNode focus = currentDir.getFolder(path);
        focus = peekContent(focus);
        if (focus == null){
            return new ExecResult(ExitStatus.FETCH_CONTEXT_FAIL,null,"Failed to fetch target directory");
        }
        currentDir.setFolder(focus,path);
        return new ExecResult(ExitStatus.SUCCESS,null,null);
    }

    public FileNode findFile(String name){
        return findFile(name,currentDir);
    }

    private FileNode findFile(String name, FileNode node){
        if (node.getFileName().equals(name) && !node.isDir()){
            return node;
        }
        if (node.isDir()){
            FileNode result = null;
            for (FileNode child : node.getChildren()){
                result = findFile(child.getFileName(), child);
                if (result != null){
                    return result;
                }
            }
        }
        return null;
    }

    private FileNode peekContent(FileNode fileNode) {
        if(fileNode.isContentKnown()) {
            return fileNode;
        }
        ExecResult returnStuff = sshManager.executeCommandAtDir("ls -F",fileNode.getPath());
        if(!returnStuff.isSuccess()) {
            return null;
        }
        String[] AnnotatedFileList = returnStuff.output().split("\n");
        System.out.println(returnStuff.output());
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

    private ExecResult shiftFocus(){
        if(browseHistory.isEmpty()){
            return new ExecResult(ExitStatus.EMPTY_BROWSE_HISTORY,null,"There is no browse history.");
        }
        currentDir = browseHistory.pop();
        return new ExecResult(ExitStatus.SUCCESS,null,null);
    }

    private ExecResult shiftFocus(FileNode newCurrentDir) {
        if (newCurrentDir == null){
            return new ExecResult(ExitStatus.FETCH_CONTEXT_FAIL,null,"Failed to fetch target directory");
        }
        if (browseHistory.size() == browseHistorySize) {
            browseHistory.removeLast();
        }
        browseHistory.addFirst(currentDir);
        currentDir = newCurrentDir;
        if (!currentDir.isContentKnown()) {
            currentDir = peekContent(currentDir);
        }
        return new ExecResult(ExitStatus.SUCCESS,null,null);
    }
}
