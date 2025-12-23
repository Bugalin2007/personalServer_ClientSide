package com.bugalin.command;

import com.bugalin.command.base.AbstractSubCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.data.SshManagerData;
import com.bugalin.handler.ConfigHandler;

public class SSHConnectionConfig extends AbstractSubCommand {
    private final ConfigHandler configHandler;

    public SSHConnectionConfig(SSHConnection parentCommand) {
        super(parentCommand, "config", new String[]{"cfg"});
        this.configHandler = parentCommand.getConfigHandler();
    }

    @Override
    public ExecResult execute(CommandContext context) {
        String[] args = context.getArgs();
        String operation = args.length != 0 ? args[0] : "";
        SshManagerData sshManagerData;
        switch (operation) {
            case "query" -> {
                sshManagerData = configHandler.getSshManagerData();
                return new ExecResult(ExitStatus.SUCCESS, "-----[SSH Connection Configuration]-----\nHost: " + sshManagerData.getHost()
                        + "\nPort: " + sshManagerData.getPort()
                        + "\nUsername: " + sshManagerData.getUsername()
                        + "\nPrivateKeyPath: " + sshManagerData.getPrivateKeyPath()
                        + "\nKnownHostsPath: " + sshManagerData.getKnownHostsPath(), null);
            }
            case "reset" -> {
                sshManagerData = SshManagerData.getdefault();
                configHandler.setSshManagerData(sshManagerData);
                return new ExecResult(ExitStatus.SUCCESS, "SSH Config reset.", null);
            }
            case "modify" -> {
                if (args.length < 3) {
                    return new ExecResult(ExitStatus.INVALID_ARGUMENT, null, "Not enough arguments.");
                }
                sshManagerData = configHandler.getSshManagerData();
                boolean flag = true;
                switch (args[1]) {
                    case "host" -> sshManagerData.setHost(args[2]);
                    case "port" -> sshManagerData.setPort(Integer.parseInt(args[2]));
                    case "username" -> sshManagerData.setUsername(args[2]);
                    case "privateKeyPath" -> sshManagerData.setPrivateKeyPath(args[2]);
                    case "knownHostsPath" -> sshManagerData.setKnownHostsPath(args[2]);
                    default -> flag = false;
                }
                if (flag) {
                    configHandler.setSshManagerData(sshManagerData);
                    return new ExecResult(ExitStatus.SUCCESS, null, null);
                } else {
                    return new ExecResult(ExitStatus.INVALID_ARGUMENT, null, "Invalid parameter name.");
                }
            }
            default -> {
                return new ExecResult(ExitStatus.INVALID_ARGUMENT,null,"Unknown operation on ssh config.");
            }
        }
    }

    @Override
    public String getLiteralName() {
        return "SSH Connection Config";
    }

    @Override
    public String getDescription() {
        return "Query and modify ssh parameters";
    }

    @Override
    public String getUsage() {
        return "ssh config [query|reset|modify]";
    }
}
