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

    public FileNode[] getChildren() { return  this.children; }

    public FileNode setContent(FileNode[] children) {
        this.dirContentUnknown = false;
        this.children = children;
        return this;
    }

    public boolean isContentKnown() {
        return !dirContentUnknown;
    }

    public boolean isDir(){
        return this.fileType == FileType.DIRECTORY;
    }

    public enum FileType {
        NORMAL,DIRECTORY,EXECUTABLE,LINK
    }

    public FileNode getFolder(String path) {
        String[] paths = path.split("/");
        int pointer = 0;
        FileNode focus = this;
        boolean flag;
        while(pointer < paths.length){
            flag = false;
            for (FileNode file : focus.getChildren()) {
                if (file.getFileName().equals(paths[pointer])) {
                    focus = file;
                    pointer++;
                    flag = true;
                    break;
                }
            }
            if (!flag){
                return null;
            }
        }
        return focus;
    }

    public void setFolder(FileNode newNode, String path) {
        String[] paths = path.split("/");
        int pointer = 0;
        FileNode parent = this;
        FileNode focus = this;

        while (pointer < paths.length - 1) {
            for (FileNode file : focus.getChildren()) {
                if (file.getFileName().equals(paths[pointer])) {
                    parent = focus;
                    focus = file;
                    pointer++;
                    break;
                }
            }
            FileNode[] children = parent.getChildren();

            for (int i = 0; i < children.length; i++) {
                if (children[i].getFileName().equals(paths[paths.length - 1])) {
                    children[i] = newNode;
                    break;
                }
            }
        }
    }

    public String toTreeString() {
        StringBuilder sb = new StringBuilder();
        buildTreeString(this, "", true, sb);
        return sb.toString();
    }

    private void buildTreeString(FileNode node, String prefix, boolean isLast, StringBuilder sb) {
        sb.append(prefix);
        if (!prefix.isEmpty()) {
            sb.append(isLast ? "└── " : "├── ");
        }

        sb.append(node.getFileName()).append("\n");

        if (node.fileType == FileType.DIRECTORY && node.isContentKnown() && node.children.length > 0) {
            String childPrefix = prefix + (isLast ? "    " : "│   ");

            for (int i = 0; i < node.children.length; i++) {
                boolean childIsLast = (i == node.children.length - 1);
                buildTreeString(node.children[i], childPrefix, childIsLast, sb);
            }
        }
    }
}
