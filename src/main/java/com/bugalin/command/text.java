/*
    private int commandFinder(String[] args) {
        switch(args[0]) {
            case "open" -> {}
            case "peek" -> {}
            case "current" -> {}
        }
        return 0;
    }

    private int commandSshCon(String[] args) {
        switch (args[0]) {
            case "status" -> {
                System.out.println(sshManager.getStatus(false));
                return 0;
            }
            case "connect" -> {
                return sshManager.connect();
            }
            case "disconnect" -> {
                return sshManager.disconnect();
            }
            case "ping", "pingip" -> {
                Socket socket = new Socket();
                SshManagerData sshManagerData = configHandler.getConfig().getSshData();
                try {
                    socket.connect(new java.net.InetSocketAddress(sshManagerData.getHost(), sshManagerData.getPort()), 3000);
                    System.out.println("[SSH Connection] Ping succeed at " + sshManagerData.getHost() + ":" + sshManagerData.getPort());
                    return 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("[SSH Connection] Ping failed at " + sshManagerData.getHost() + ":" + sshManagerData.getPort());
                    return 2;
                }
            }
            case "reload" -> {
                sshManager.setData(configHandler.getConfig().getSshData());
                return sshManager.initialize();
            }
            default -> {
                return reject();
            }
        }
    }

    private int commandConfig(String[] args) {
        SshManagerData sshManagerData = configHandler.getConfig().getSshData();
        switch (args[0]) {
            case "query" -> {
                String message;
                switch (args[1]) {
                    case "host" -> message = "[Config] Current host is " + sshManagerData.getHost();
                    case "username" -> message = "[Config] Current username is " + sshManagerData.getUsername();
                    case "port" -> message = "[Config] Current port is " + sshManagerData.getPort();
                    case "privateKeyPath" -> message = "[Config] Private key path is " + sshManagerData.getPrivateKeyPath();
                    case "knownHostsPath" -> message = "[Config] Known hosts path is " + sshManagerData.getKnownHostsPath();
                    default -> message = " [Config] Current SSH parameters: \n |  host : " + sshManagerData.getHost()
                                         + "\n |  username : " + sshManagerData.getUsername()
                                         + "\n |  port : " + sshManagerData.getPort()
                                         + "\n |  privateKeyPath : " + sshManagerData.getPrivateKeyPath()
                                         + "\n |  knownHostsPath : " + sshManagerData.getKnownHostsPath();
                }
                System.out.println(message);
            }
            case "modify" -> {
                System.out.print("[Config] Enter new value for the parameter\n> ");
                String newValue = scanner.next();
                switch (args[1]) {
                    case "host" -> sshManagerData.setHost(newValue);
                    case "username" -> sshManagerData.setUsername(newValue);
                    case "port" -> sshManagerData.setPort(Integer.parseInt(newValue));
                    case "privateKeyPath" -> sshManagerData.setPrivateKeyPath(newValue);
                    case "knownHostsPath" -> sshManagerData.setKnownHostsPath(newValue);
                    default -> {
                        return reject("[ERROR] Config parameter not found!");
                    }
                }
                System.out.println("[Config] Parameter changed successfully!");
            }
            case "reset" -> {
                switch (args[1]) {
                    case "host" -> sshManagerData.setHost("10.129.240.97");
                    case "username" -> sshManagerData.setUsername("ubuntu");
                    case "port" -> sshManagerData.setPort(22);
                    case "privateKeyPath" -> sshManagerData.setPrivateKeyPath("C:/Users/19364/.ssh/keyBugalin.pem");
                    case "knownHostsPath" -> sshManagerData.setKnownHostsPath("C:/Users/19364/.ssh/known_hosts");
                    default -> {
                        return reject("[ERROR] Config parameter not found!");
                    }
                }
                System.out.println("[Config] Parameter reset successfully!");
                configHandler.setSshManagerData(sshManagerData);
            }
            default -> {
                return reject();
            }
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
/*
    CONFIG("config",new String[]{"cfg"},0,new Command[]{CONFIG_QUERY,CONFIG_MODIFY,CONFIG_RESET}),

    SSH_CONNECTION("ssh",new String[]{"sshConnection","sshcon","connection","network"},false,true,-1),
    FINDER("finder",new String[]{"fnd","files",},false,true,-1);


     */
