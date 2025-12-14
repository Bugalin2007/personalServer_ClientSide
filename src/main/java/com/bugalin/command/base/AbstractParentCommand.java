package com.bugalin.command.base;

import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;

public abstract class AbstractParentCommand extends AbstractCommand implements Command {

    protected AbstractParentCommand(String name,
                                 String[] aliases) {
        super(name, aliases);
    }

    public ExecResult execute(CommandContext context) {
        return new ExecResult(ExitStatus.UNKNOWN_ERROR,null,null);
    }

    @Override
    public String getLiteralName() {
        return "[NO VALID NAME]";
    }

    @Override
    public String getDescription() {
        return "[NO VALID DESCRIPTION]";
    }

    @Override
    public String getUsage() {
        return "[NO VALID USAGE]";
    }
}
