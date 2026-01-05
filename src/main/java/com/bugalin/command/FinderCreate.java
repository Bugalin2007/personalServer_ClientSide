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
import java.io.IOException;
import java.nio.file.Path;


public class FinderCreate extends AbstractSubCommand {
    private final RemoteFileHandler remoteFileHandler;
    private final SSHManager sshManager;

    public FinderCreate(Finder parentCommand) {
        super(parentCommand, "create", new String[] {"make","new"});
        this.remoteFileHandler = parentCommand.getRemoteFileHandler();
        this.sshManager = parentCommand.getSSHManager();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        String[] args = context.getArgs();
        if(args.length == 0){ return  new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"No args provided");}
        if(args[0].equals("dir")) {
            return sshManager.executeCommandAtDir("mkdir -p "+args[1],remoteFileHandler.getCurrentDirPath());
        }else if(args[0].equals("file")){
            File file = new File("temp");
            try {
                file.createNewFile();
                int value = SwingTextEditor.edit(file);
                if (value == -1){
                    return new ExecResult(ExitStatus.NONEXISTENT_FILE,null,"Error in opening local temp file");
                }
                ExecResult result = sshManager.uploadFile("Temp File", file.getPath(), remoteFileHandler.getCurrentDirPath()+"/"+args[1]);
                if (result.isSuccess()){
                    file.delete();
                }
                return result;
            } catch (IOException e) {
                return  new ExecResult(ExitStatus.UNKNOWN_ERROR,null,e.getMessage());
            }
        }else{
            return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Invalid arguments.");
        }
    }

    @Override
    public String getLiteralName() {
        return "Finder Creation";
    }

    @Override
    public String getDescription() {
        return "Do file creation actions.";
    }

    @Override
    public String getUsage() {
        return "finder create [file|dir] <file>";
    }
}
