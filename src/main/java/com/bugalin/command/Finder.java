package com.bugalin.command;

import com.bugalin.command.base.AbstractParentCommand;
import com.bugalin.handler.RemoteFileHandler;

public class Finder extends AbstractParentCommand {
    public RemoteFileHandler getRemoteFileHandler() {
        return remoteFileHandler;
    }

    private final RemoteFileHandler remoteFileHandler;
    public Finder(RemoteFileHandler remoteFileHandler) {
        super("finder",new String[]{"files"});
        this.remoteFileHandler = remoteFileHandler;
    }
}
