package com.bugalin.command;

import com.bugalin.command.base.AbstractParentCommand;
import com.bugalin.handler.ConfigHandler;
import com.bugalin.handler.SSHManager;

public class SSHConnection extends AbstractParentCommand {
    private final ConfigHandler configHandler;

    protected ConfigHandler getConfigHandler() {
        return configHandler;
    }

    protected SSHManager getSshManager() {
        return sshManager;
    }

    private final SSHManager sshManager;
    public SSHConnection(ConfigHandler configHandler, SSHManager sshManager) {
        super("ssh",new String[]{"network"});
        this.configHandler = configHandler;
        this.sshManager = sshManager;
    }

}
