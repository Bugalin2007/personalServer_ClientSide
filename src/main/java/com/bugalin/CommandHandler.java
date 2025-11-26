package com.bugalin;

import java.util.Arrays;

public class CommandHandler {
    private final SSHManager sshManager;
    public CommandHandler(SSHManager sshManager) {
        this.sshManager = sshManager;
    }

    public int execute(String input) {
        String[] args = input.split(" ");
        String commandName = args[0].toLowerCase();
        args = Arrays.copyOfRange(args, 1, args.length);
        Command command;
        try {
            command = Command.fromName(commandName);
            int argumentCount = command.getArgumentCount();
            if (argumentCount != args.length) {
                throw new IllegalArgumentException("Command " + commandName + " has no enough arguments");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return 1;
        }
        switch (command) {
            case QUIT_PROGRAM -> {
                return -1;
            }
            case TEST -> {
                if(args[0].equals("exec")) {
                    sshManager.ChannelExec();
                }else if(args[0].equals("sftp")) {
                    sshManager.ChannelSftp();
                }
                return 0;
            }
            default -> {
                return 0;
            }
        }
    }
}
