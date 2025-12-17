package com.bugalin.command;

import com.bugalin.command.base.AbstractParentCommand;

public class SSHConnection extends AbstractParentCommand {
    public SSHConnection() {
        super("ssh",new String[]{"network"});
    }
}
