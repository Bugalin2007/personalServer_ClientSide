package com.bugalin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ClientInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static SSHManager sshManager;
    private static final ConfigHandler configHandler = new ConfigHandler();
    private static CommandHandler commandHandler;

    private static void launch(){
        System.out.println("[ServerBugalin Client] Launching...");
        try {
            System.out.println("|   ...Reading Config...   ");
            configHandler.readConfig();
            sshManager = new SSHManager(configHandler.getConfig().getSshData());
            System.out.println("|   ...Building SSH Connection...   ");
            sshManager.initialize();
            sshManager.connect();
            System.out.println("|   ...Activating Shell Interpreter...   ");
            commandHandler = new CommandHandler(sshManager,configHandler);
        } catch (IOException e) {
            System.out.println("[ERROR] Error during launching!");
            e.printStackTrace();
        }
        System.out.println("[ServerBugalin Client] Launched Successfully");
    }

    private static void shutdown(){
        System.out.println("[ServerBugalin Client] Shutting down...");
        System.out.println("|   ...Terminating SSH Connection...   ");
        sshManager.disconnect();
        try {
            System.out.println("|   ...Saving Config...   ");
            configHandler.saveConfig();
        } catch (IOException e) {
            System.out.println("[ERROR] Error during shutting down!");
            e.printStackTrace();
        }
        System.out.println("[ServerBugalin Client] Shutdown Successfully");
    }

    private static void runtime() {
        String commandText;
        while(true){
            System.out.print("> ");
            commandText = scanner.nextLine();
            int status = 1;
            String[] args = commandText.split(" ");
            String commandName = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            Command command;
            try {
                command = Command.fromName(commandName);
                int argumentCount = command.getArgumentCount();
                if (argumentCount < args.length) {
                    throw new IllegalArgumentException("[ERROR] Command " + commandName + " has too many arguments");
                }else if (argumentCount > args.length) {
                    throw new IllegalArgumentException("[ERROR] Command " + commandName + " has no enough arguments");
                }
                status = commandHandler.execute(command,args);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (status == -1){
                break;
            }
        }
    }

    public static void main(String[] args) {
        launch();
        runtime();
        shutdown();
    }
}
