package com.bugalin.command;

import com.bugalin.command.base.AbstractCommand;
import com.bugalin.command.base.CommandContext;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;

public class QuitProgram extends AbstractCommand {

    public QuitProgram() {
        super("q",new String[]{"quit","exit"});
    }

    @Override
    public ExecResult execute(CommandContext context) {
        return new ExecResult(ExitStatus.EXIT_PROGRAM,null,null);
    }

    @Override
    public String getLiteralName() {
        return "Quit Program Command";
    }

    @Override
    public String getDescription() {
        return "Quit program.";
    }

    @Override
    public String getUsage() {
        return "exit";
    }
}
