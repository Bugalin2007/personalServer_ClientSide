package com.bugalin;

import com.bugalin.command.QuitProgram;
import com.bugalin.command.SSHConnection;
import com.bugalin.command.SSHConnectionConfig;
import com.bugalin.command.SSHConnectionConnect;
import com.bugalin.command.base.Command;
import com.bugalin.command.base.CommandDispatcher;
import com.bugalin.command.base.CommandRegister;
import com.bugalin.data.ExecResult;
import com.bugalin.data.ExitStatus;
import com.bugalin.handler.ConfigHandler;
import com.bugalin.handler.SSHManager;

import java.util.Scanner;

public class CLIApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static SSHManager sshManager;
    private static final ConfigHandler configHandler = new ConfigHandler();
    private static final CommandRegister commandRegister = new CommandRegister();
    private static CommandDispatcher commandDispatcher;

    private static void launch(){
        configHandler.readConfig();
        sshManager = new SSHManager(configHandler.getSshManagerData());
        sshManager.initialize();
        sshManager.connect();
        register();
        commandDispatcher = new CommandDispatcher(commandRegister);
    }

    private static void register() {
        commandRegister.register(new QuitProgram());
        Command ssh = new SSHConnection();
        commandRegister.register(ssh);
        commandRegister.registerSubCommand(new SSHConnectionConfig(ssh,configHandler));
        commandRegister.registerSubCommand(new SSHConnectionConnect(ssh,configHandler,sshManager));
    }

    private static void shutdown(){
        sshManager.disconnect();
        configHandler.saveConfig();
    }

    private static void runtime() {
        ExecResult commandResult = new ExecResult(ExitStatus.SUCCESS,"Program Launched",null);
        do {
            System.out.println((commandResult.isSuccess()?"[Server Terminal]"+commandResult.output()
                    :"[ERROR]"+commandResult.error()));
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
