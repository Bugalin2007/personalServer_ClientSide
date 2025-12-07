package com.bugalin.command.base;

public record CommandContext(Command command, String[] args) {

    public String getArg(int index) {
        return index < args.length ? args[index] : null;
    }

    public String[] getArgs() {
        return args;
    }

    public int getArgCount() {
        return args.length;
    }
}