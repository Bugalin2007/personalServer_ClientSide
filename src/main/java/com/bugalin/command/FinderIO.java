package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.data.FileNode;
import com.bugalin.handler.RemoteFileHandler;
import com.bugalin.handler.SSHManager;
import com.bugalin.handler.SwingTextEditor;

import java.io.File;


public class FinderIO extends AbstractSubCommand {
    private final RemoteFileHandler remoteFileHandler;
    private final SSHManager sshManager;

    public FinderIO(Finder parentCommand) {
        super(parentCommand, "io", new String[] {"transfer","fileTransfer"});
        this.remoteFileHandler = parentCommand.getRemoteFileHandler();
        this.sshManager = parentCommand.getSSHManager();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        String[] args = context.getArgs();
        if(args.length == 0){ return  new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"No args provided");}
        if(args[0].equals("upload")) {
            String[] filePaths = args[1].split("/");
            String fileName = filePaths[filePaths.length - 1];
            String localPath = args[1];
            String remotePath = (args.length > 2) ? args[2] : remoteFileHandler.getUploadPath();
            return sshManager.uploadFile(fileName, localPath, remotePath);
        }else if(args[0].equals("uploadhere")) {
            String[] filePaths = args[1].split("/");
            String fileName = filePaths[filePaths.length - 1];
            String localPath = args[1];
            String remotePath = remoteFileHandler.getCurrentDirPath();
            return sshManager.uploadFile(fileName, localPath, remotePath);
        }else if(args[0].equals("download")){
            String[] filePaths = args[1].split("/");
            String fileName = filePaths[filePaths.length-1];
            String remotePath = args[1];
            String localPath = (args.length > 2) ? args[2] : remoteFileHandler.getDownloadPath();
            return sshManager.downloadFile(fileName,localPath,remotePath);
        } else if(args[0].equals("downloadhere")) {
        String fileName = args[1];
        System.out.println("[DEBUG] Looking for file: " + fileName);
        System.out.println("[DEBUG] Current directory: " + remoteFileHandler.getCurrentDirPath());

        FileNode targetFile = remoteFileHandler.findFile(fileName);
        if (targetFile == null) {
            System.out.println("[DEBUG] File not found in current directory tree");
            return new ExecResult(ExitStatus.NONEXISTENT_FILE, null, "No such file or directory, did you open its parent directory?");
        }

        System.out.println("[DEBUG] Found file at: " + targetFile.getPath());
        System.out.println("[DEBUG] File is directory: " + targetFile.isDir());

        String remotePath = targetFile.getPath();
        String localPath = (args.length > 2) ? args[2] : remoteFileHandler.getDownloadPath();
        return sshManager.downloadFile(fileName, localPath, remotePath);
    }else if(args[0].equals("edit")){
            String fileName = args[1];
            FileNode targetFile = remoteFileHandler.findFile(fileName);
            if (targetFile == null) {
                return new ExecResult(ExitStatus.NONEXISTENT_FILE, null, "No such file or directory, did you open its parent directory?");
            }
            String remotePath = targetFile.getPath();
            String localPath = remoteFileHandler.getDownloadPath();
            ExecResult intermediateResult = sshManager.downloadFile(fileName, localPath, remotePath);
            if (!intermediateResult.isSuccess()){
                return intermediateResult;
            }
            File temp = new File(localPath+fileName);
            int value = SwingTextEditor.edit(temp);
            if (value == -1){
                return new ExecResult(ExitStatus.NONEXISTENT_FILE,null,"Error in opening local temp file");
            }else if (value == 1){
                return new ExecResult(ExitStatus.SUCCESS,"Did not edit file",null);
            }
            intermediateResult = sshManager.uploadFile(fileName, localPath+fileName, remotePath);
            if(intermediateResult.isSuccess()){
                temp.delete();
            }
            return intermediateResult;
        }else{
            return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Invalid arguments.");
        }
    }

    @Override
    public String getLiteralName() {
        return "Finder Upload and Download";
    }

    @Override
    public String getDescription() {
        return "Do file transfer actions.";
    }

    @Override
    public String getUsage() {
        return "finder io [upload|download] <file>";
    }
}
