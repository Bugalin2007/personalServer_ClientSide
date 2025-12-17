package com.bugalin.command.base;

import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;

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
            return new ExecResult(ExitStatus.UNKNOWN_COMMAND,null, commandName + " " + (hasSubCommand ? subCommandName : ""));
        }

        args = Arrays.copyOfRange(input, hasSubCommand ? 2 : 1, input.length);

        if(args.length == 1 && args[0].equals("help")) {
            CommandHelp(command);
            return new ExecResult(ExitStatus.SUCCESS,null,null);
        }

        CommandContext context = new CommandContext(command,args);

        ExecResult result = command.execute(context);
        if(result.isCommandUsageProblem()){
            CommandHelp(command);
        }
        return result;
    }

    public void CommandHelp(Command command){
        String help = "---------" +
                command.getLiteralName() +
                "---------\nDescription:" +
                command.getDescription() +
                "\nUsage:" +
                command.getUsage();
        System.out.println(help);
    }
}