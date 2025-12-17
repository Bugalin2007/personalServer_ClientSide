package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.handler.RemoteFileHandler;

public class FinderView extends AbstractSubCommand {
    private final RemoteFileHandler remoteFileHandler;

    public FinderView(Finder parentCommand) {
        super(parentCommand, "view", null);
        this.remoteFileHandler = parentCommand.getRemoteFileHandler();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        return null;
    }

    @Override
    public String getLiteralName() {
        return "Finder";
    }

    @Override
    public String getDescription() {
        return "Open and switch focus to selected directory.";
    }

    @Override
    public String getUsage() {
        return "finder view <Dir Name>";
    }
}
