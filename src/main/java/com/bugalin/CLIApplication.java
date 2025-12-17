package com.bugalin;

import com.bugalin.command.*;
import com.bugalin.command.base.Command;
import com.bugalin.command.base.CommandDispatcher;
import com.bugalin.command.base.CommandRegister;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.handler.ConfigHandler;
import com.bugalin.handler.RemoteFileHandler;
import com.bugalin.handler.SSHManager;

import java.util.Scanner;

public class CLIApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static SSHManager sshManager;
    private static RemoteFileHandler remoteFileHandler;
    private static final ConfigHandler configHandler = new ConfigHandler();
    private static final CommandRegister commandRegister = new CommandRegister();
    private static CommandDispatcher commandDispatcher;

    private static void launch(){
        configHandler.readConfig();
        sshManager = new SSHManager(configHandler.getSshManagerData());
        sshManager.initialize();
        sshManager.connect();
        remoteFileHandler = new RemoteFileHandler(configHandler.getFileHandlerData(),sshManager);
        register();
        commandDispatcher = new CommandDispatcher(commandRegister);
    }

    private static void register() {
        commandRegister.register(new QuitProgram());
        SSHConnection ssh = new SSHConnection(configHandler,sshManager);
        commandRegister.register(ssh);
        commandRegister.registerSubCommand(new SSHConnectionConfig(ssh));
        commandRegister.registerSubCommand(new SSHConnectionConnect(ssh));
        Finder finder = new Finder();
        commandRegister.register(finder);
        commandRegister.registerSubCommand(new FinderView(finder));
    }

    private static void shutdown(){
        sshManager.disconnect();
        configHandler.saveConfig();
    }

    private static void runtime() {
        ExecResult commandResult;
        do {
            System.out.print("\n> ");
            commandResult = commandDispatcher.dispatch(scanner.nextLine());
            printOutput(commandResult);
        } while (!commandResult.isExit());
    }

    private static void printOutput(ExecResult commandResult) {
        if (commandResult.isSuccess() && commandResult.output() != null) {
            System.out.println("[Server Terminal]"+commandResult.output());
        } else if (!commandResult.isSuccess() && commandResult.error() != null) {
            System.out.println("[ERROR]"+commandResult.error());
        }
    }
    public static void main(String[] args) {
        launch();
        runtime();
        shutdown();
    }
}
