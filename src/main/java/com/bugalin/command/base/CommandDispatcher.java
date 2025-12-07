package com.bugalin.command.base;

import com.bugalin.command.data.*;

import java.util.Arrays;

public class CommandDispatcher {
    private final CommandRegister commandRegister;

    public CommandDispatcher(CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
    }
    
    public ExecResult dispatch(String str) {
        String[] input = str.trim().split("\\s+");
        Command command;
        String[] args;
        boolean hasSubCommand;
        if (input.length == 0) {
            return new ExecResult(ExitStatus.EMPTY_INPUT,null,null);
        }

        String commandName = input[0];
        hasSubCommand = commandRegister.hasSubCommand(commandName);
        String subCommandName = hasSubCommand && input.length > 1 ? input[1] : null;
        command = hasSubCommand ? commandRegister.findSubCommand(commandName,subCommandName)
                : commandRegister.findCommand(commandName);

        if (command == null) {
            return new ExecResult(ExitStatus.UNKNOWN_COMMAND,null, commandName + (hasSubCommand ? subCommandName : ""));
        }

        args = Arrays.copyOfRange(input, hasSubCommand ? 2 : 1, input.length);

        CommandContext context = new CommandContext(command,args);
        return command.execute(context);
    }
}