package com.bugalin;

import com.bugalin.command.QuitProgram;
import com.bugalin.command.base.CommandDispatcher;
import com.bugalin.command.base.CommandRegister;
import com.bugalin.command.data.ExecResult;

import java.util.Scanner;

public class CLIApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static SSHManager sshManager;
    private static final ConfigHandler configHandler = new ConfigHandler();
    private static final CommandRegister commandRegister = new CommandRegister();
    private static CommandDispatcher commandDispatcher;

    private static void launch(){
        configHandler.readConfig();
        sshManager = new SSHManager(configHandler.getConfig().getSshData());
        sshManager.initialize();
        sshManager.connect();
        register();
        commandDispatcher = new CommandDispatcher(commandRegister);
    }

    private static void register() {
        commandRegister.register(new QuitProgram());
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
        } while (!commandResult.isExit());
    }

    public static void main(String[] args) {
        launch();
        runtime();
        shutdown();
    }
}
