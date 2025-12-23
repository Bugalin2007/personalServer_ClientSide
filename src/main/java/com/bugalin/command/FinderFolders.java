package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.handler.RemoteFileHandler;

public class FinderFolders extends AbstractSubCommand {
    private final RemoteFileHandler remoteFileHandler;

    public FinderFolders(Finder parentCommand) {
        super(parentCommand, "folder", new String[] {"see","look"});
        this.remoteFileHandler = parentCommand.getRemoteFileHandler();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        String[] args = context.getArgs();
        if (args.length == 0) { return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Not enough arguments");}
        switch(args[0]) {
            case "refresh" -> {
                System.out.println(remoteFileHandler.display());
                return new ExecResult(ExitStatus.SUCCESS,null,null);
            }
            case "open" -> {
                ExecResult result = remoteFileHandler.openFolder(args[1]);
                System.out.println(remoteFileHandler.display());
                return result;
            }
            case "goto" -> {
                if (args.length < 3) { return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Not enough arguments");}
                ExecResult result;
                if (args[1].equals("here")){
                    result = remoteFileHandler.changeFocusToPathInside(args[2]);
                }else if (args[1].equals("root")){
                    result =  remoteFileHandler.changeFocusToPath(args[2]);
                }else{
                    return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Invalid arguments");
                }
                System.out.println(remoteFileHandler.display());
                return result;
            }
            case "return" -> {
                ExecResult result = remoteFileHandler.seeLast();
                System.out.println(remoteFileHandler.display());
                return result;
            }
            default -> {
                return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Invalid arguments");
            }
        }
    }

    @Override
    public String getLiteralName() {
        return "Finder Browser";
    }

    @Override
    public String getDescription() {
        return "Browse files at current directory.";
    }

    @Override
    public String getUsage() {
        return "finder folder [refresh|open|goto|return] <fileName>";
    }
}
