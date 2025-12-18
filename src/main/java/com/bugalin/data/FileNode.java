package com.bugalin.data;

public class FileNode {
    private final String path;
    private final String fileName;
    private final FileType fileType;
    private FileNode[] children;
    private boolean dirContentUnknown;

    public FileNode(String path, String fileName, FileType fileType) {
        this.path = path;
        this.fileName = fileName;
        this.fileType = fileType;
        this.children = new FileNode[0];
        this.dirContentUnknown = false;
    }
    public FileNode(String path, String fileName, FileNode[] children) {
        this.path = path;
        this.fileName = fileName;
        this.fileType = FileType.DIRECTORY;
        this.children = children;
        this.dirContentUnknown = false;
    }
    public FileNode(String path, String fileName, boolean dirContentUnknown) {
        this.path = path;
        this.fileName = fileName;
        this.fileType = FileType.DIRECTORY;
        this.children = new FileNode[0];
        this.dirContentUnknown = true;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public FileNode setContent(FileNode[] children) {
        this.dirContentUnknown = false;
        this.children = children;
        return this;
    }

    public boolean isContentKnown() {
        return !dirContentUnknown;
    }

    public enum FileType {
        NORMAL,DIRECTORY,EXECUTABLE,LINK;
    }
}
