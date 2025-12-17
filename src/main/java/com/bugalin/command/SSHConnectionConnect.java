package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.Command;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.data.SshManagerData;
import com.bugalin.handler.ConfigHandler;
import com.bugalin.handler.SSHManager;

import java.io.IOException;
import java.net.Socket;

public class SSHConnectionConnect extends AbstractSubCommand {
    private final ConfigHandler configHandler;
    private final SSHManager sshManager;

    public SSHConnectionConnect(SSHConnection parentCommand) {
        super(parentCommand, "connection", new String[]{"con"});
        this.configHandler = parentCommand.getConfigHandler();
        this.sshManager = parentCommand.getSshManager();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        String[] args = context.getArgs();
        String operation = args.length != 0 ? args[0] : "";
        switch (operation) {
            case "check","status" -> {
                return new ExecResult(ExitStatus.SUCCESS, sshManager.getStatus(), null);
            }
            case "init" -> {
                sshManager.setData(configHandler.getSshManagerData());
                return sshManager.initialize();
            }
            case "ping","try" -> {
                Socket socket = new Socket();
                SshManagerData sshManagerData = configHandler.getSshManagerData();
                boolean pingSuccess = true;
                try {
                    socket.connect(new java.net.InetSocketAddress(sshManagerData.getHost(), sshManagerData.getPort()), 3000);
                } catch (IOException e) {
                    pingSuccess = false;
                }
                return new ExecResult(ExitStatus.SUCCESS, "Ping "+ (pingSuccess ? "succeed" : "failed") +" at \" + sshManagerData.getHost() + \":\" + sshManagerData.getPort()", null);
            }
            case "connect" -> {
                return sshManager.connect();
            }
            case "disconnect" -> {
                return sshManager.disconnect();
            }
            default -> {
                return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Unknown operation on ssh config.");
            }
        }
    }

    @Override
    public String getLiteralName() {
        return "SSH Connection Main";
    }

    @Override
    public String getDescription() {
        return "Build, try, connect and disconnect ssh.";
    }

    @Override
    public String getUsage() {
        return "ssh connection [status|init|ping|connect|disconnect]";
    }
}
