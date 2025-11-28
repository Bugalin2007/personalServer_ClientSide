package com.bugalin;


import com.bugalin.data.SshData;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CommandHandler {
    private final SSHManager sshManager;
    private static final Scanner scanner = new Scanner(System.in);
    private final ConfigHandler configHandler;
    public CommandHandler(SSHManager sshManager, ConfigHandler configHandler) {
        this.sshManager = sshManager;
        this.configHandler = configHandler;
    }

    public int execute(Command command,String[] args) {
        switch (command) {
            case QUIT_PROGRAM -> {
                return -1;
            }
            case CONFIG -> {
                return commandConfig(args);
            }
            case SSH_CONNECTION -> {
                switch (args[0]) {
                    case "status" -> {
                        System.out.println(sshManager.getStatus(false));
                    }
                    case "connect" -> {
                        sshManager.connect();
                    }
                    case "disconnect" -> {
                        sshManager.disconnect();
                    }
                    case "ping", "pingip" -> {
                        Socket socket = new Socket();
                        SshData sshData = configHandler.getConfig().getSshData();
                        try {
                            socket.connect(new java.net.InetSocketAddress(sshData.getHost(), sshData.getPort()), 3000);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("[SSH Connection] Ping failed at " + sshData.getHost() + ":" + sshData.getPort());
                        }
                        System.out.println("[SSH Connection] Ping succeed at " + sshData.getHost() + ":" + sshData.getPort());
                    }
                    case "reload" -> {
                        sshManager.setData(configHandler.getConfig().getSshData());
                        sshManager.initialize();
                    }
                    default -> {return reject();}
                }
            }
            case TEST -> {
                if(args[0].equals("exec")) {
                    sshManager.ChannelExec();
                }else if(args[0].equals("sftp")) {
                    sshManager.ChannelSftp();
                }
            }
        }
        return 0;
    }

    private int commandConfig(String[] args) {
        SshData sshData = configHandler.getConfig().getSshData();
        if (args[0].equals("query")){
            String message;
            switch (args[1]) {
                case "host" -> message = "[Config] Current host is " + sshData.getHost();
                case "username" -> message = "[Config] Current username is " + sshData.getUsername();
                case "port" -> message = "[Config] Current port is " + sshData.getPort();
                case "privateKeyPath" -> message = "[Config] Private key path is " + sshData.getPrivateKeyPath();
                case "knownHostsPath" -> message = "[Config] Known hosts path is " + sshData.getKnownHostsPath();
                default -> message = " [Config] Current SSH parameters: \n |  host : " + sshData.getHost()
                                     + "\n |  username : " + sshData.getUsername()
                                     + "\n |  port : " + sshData.getPort()
                                     + "\n |  privateKeyPath : " + sshData.getPrivateKeyPath()
                                     + "\n |  knownHostsPath : " + sshData.getKnownHostsPath();
            }
            System.out.println(message);
        }else if (args[0].equals("modify")) {
            System.out.print("[Config] Enter new value for the parameter\n> ");
            String newValue = scanner.next();
            switch (args[1]){
                case "host" -> sshData.setHost(newValue);
                case "username" -> sshData.setUsername(newValue);
                case "port" -> sshData.setPort(Integer.parseInt(newValue));
                case "privateKeyPath" -> sshData.setPrivateKeyPath(newValue);
                case "knownHostsPath" -> sshData.setKnownHostsPath(newValue);
                default -> {
                    return reject("[ERROR] Config parameter not found!");
                }
            }
            System.out.println("[Config] Parameter changed successfully!");
        }else if (args[0].equals("reset")) {
            switch (args[1]) {
                case "host" -> sshData.setHost("10.129.240.97");
                case "username" -> sshData.setUsername("ubuntu");
                case "port" -> sshData.setPort(22);
                case "privateKeyPath" -> sshData.setPrivateKeyPath("C:/Users/19364/.ssh/keyBugalin.pem");
                case "knownHostsPath" -> sshData.setKnownHostsPath("C:/Users/19364/.ssh/known_hosts");
                default -> {
                    return reject("[ERROR] Config parameter not found!");
                }
            }
            System.out.println("[Config] Parameter reset successfully!");
        }else {
            return reject();
        }
        return 0;
    }

    private static int reject(String message) {
        System.out.println(message);
        return 1;
    }

    private static int reject(){
        System.out.println("[ERROR] Unknown subcommand!");
        return 1;
    }
}
