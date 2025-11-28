package com.bugalin;

public enum Command {
    QUIT_PROGRAM("quitProgram",new String[]{"exit","q","quit"},0),
    TEST("test",new String[0],1),
    CONFIG("config",new String[]{"cfg"},2),
    SSH_CONNECTION("sshConnection",new String[]{"ssh","sshcon","connection","network"},1),;

    private final String name;
    private final String[] aliases;
    private final int argumentCount;

    Command(String name, String[] aliases, int argumentCount) {
        this.name = name;
        this.aliases = aliases;
        this.argumentCount = argumentCount;
    }

    public static Command fromName(String text) {
        for (Command command : Command.values()) {
            if (command.name.equalsIgnoreCase(text)) {
                return command;
            }
        }
        String[] aliases;
        for (Command command : Command.values()) {
            aliases = command.aliases;
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(text)) {
                    return command;
                }
            }
        }
        throw new IllegalArgumentException("Command " + text + " not found");
    }

    public int getArgumentCount() {
        return argumentCount;
    }
}
