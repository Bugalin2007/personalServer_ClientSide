package com.bugalin.command.base;

public abstract class AbstractCommand implements Command {
    private final String name;
    private final String[] aliases;

    protected AbstractCommand(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }
}

