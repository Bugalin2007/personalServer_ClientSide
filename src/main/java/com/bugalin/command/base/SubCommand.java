package com.bugalin.command.base;

public interface SubCommand extends Command {
    Command getParentCommand();
}
