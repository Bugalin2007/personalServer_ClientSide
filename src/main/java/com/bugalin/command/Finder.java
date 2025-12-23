package com.bugalin.command;

import com.bugalin.command.base.AbstractParentCommand;
import com.bugalin.handler.RemoteFileHandler;
import com.bugalin.handler.SSHManager;

public class Finder extends AbstractParentCommand {
    public RemoteFileHandler getRemoteFileHandler() {
        return remoteFileHandler;
    }
    public SSHManager getSSHManager() {return sshManager;}

    private final RemoteFileHandler remoteFileHandler;
    private final SSHManager sshManager;
    public Finder(RemoteFileHandler remoteFileHandler,  SSHManager sshManager) {
        super("finder",new String[]{"files"});
        this.remoteFileHandler = remoteFileHandler;
        this.sshManager = sshManager;
    }
}
