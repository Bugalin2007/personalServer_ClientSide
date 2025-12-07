package com.bugalin.command.base;

public abstract class AbstractParentCommand extends AbstractCommand implements Command {

    protected AbstractParentCommand(Command parentCommand, String name,
                                 String[] aliases) {
        super(name, aliases);
    }
}
