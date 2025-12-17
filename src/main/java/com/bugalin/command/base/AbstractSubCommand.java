package com.bugalin.command.base;

public abstract class AbstractSubCommand extends AbstractCommand implements SubCommand {
    private final Command parentCommand;

    public AbstractSubCommand(Command parentCommand, String name,
                                 String[] aliases) {
        super(name, aliases);
        this.parentCommand = parentCommand;
    }

    @Override
    public Command getParentCommand() {
        return parentCommand;
    }
}
