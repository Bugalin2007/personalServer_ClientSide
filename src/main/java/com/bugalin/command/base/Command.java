package com.bugalin.command.base;

import com.bugalin.data.ExecResult;

public interface Command {
    String getName();
    String[] getAliases();
    ExecResult execute(CommandContext context);
    String getLiteralName();
    String getDescription();
    String getUsage();
}
