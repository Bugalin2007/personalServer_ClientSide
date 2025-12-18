package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.handler.RemoteFileHandler;

public class FinderFolders extends AbstractSubCommand {
    private final RemoteFileHandler remoteFileHandler;

    public FinderFolders(Finder parentCommand) {
        super(parentCommand, "folder", new String[] {"see","look"});
        this.remoteFileHandler = parentCommand.getRemoteFileHandler();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        return null;
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
        return "finder folder [refresh|open|goto] <fileName>";
    }
}
