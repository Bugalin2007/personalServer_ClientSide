package com.bugalin;

import java.util.Scanner;

public class ClientInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SSHManager sshManager = new SSHManager();
    private static final CommandHandler commandHandler = new CommandHandler(sshManager);

    private static void launch(){
        System.out.println("[ServerBugalin Client Interface] Launching...");
        sshManager.initialize();
        sshManager.connect();
        System.out.println("[ServerBugalin Client Interface] Launched Successfully");
    }

    private static void shutdown(){
        System.out.println("[ServerBugalin Client Interface] Shutting down...");
        sshManager.disconnect();
        System.out.println("[ServerBugalin Client Interface] Shutdown Successfully");
    }


    private static void runtime() {
        String command;
        while(true){
            command = scanner.nextLine();
            int status = commandHandler.execute(command);
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
