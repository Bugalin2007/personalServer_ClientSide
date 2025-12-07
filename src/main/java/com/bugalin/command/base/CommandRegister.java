package com.bugalin.command.base;

import java.util.HashMap;
import java.util.Map;

public class CommandRegister {
    private final Map<String, Command> commandByName = new HashMap<>();
    private final Map<String, Command> commandByAlias = new HashMap<>();
    private final Map<String, Map<String, SubCommand>> subCommands = new HashMap<>();

    public void register(Command command) {
        commandByName.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commandByAlias.put(alias, command);
        }
    }

    public void registerSubCommand(SubCommand subCommand) {
        if (subCommand == null){return;}
        String parentName = subCommand.getParentCommand().getName();
        subCommands.computeIfAbsent(parentName, _ -> new HashMap<>())
                .put(subCommand.getName(), subCommand);
    }

    protected boolean hasSubCommand(String parentName) {
        return subCommands.containsKey(parentName);
    }

    protected Command findCommand(String name) {
        Command command = commandByName.get(name);
        if (command == null) {
            command = commandByAlias.get(name);
        }
        return command;
    }

    protected SubCommand findSubCommand(String commandName, String subCommandName) {
        return subCommands.get(commandName).get(subCommandName);
    }

    public void registerAll() {

    }
}