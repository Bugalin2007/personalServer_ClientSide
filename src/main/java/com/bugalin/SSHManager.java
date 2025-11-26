package com.bugalin;

import com.jcraft.jsch.*;

import java.io.InputStream;

public class SSHManager {
    public static void main(String[] args) {
        String host = "10.129.240.97";
        String username = "ubuntu";
        int port = 22;
        String privateKeyPath = "C:/Users/19364/.ssh/keyBugalin.pem";
        String knownHostsPath = "C:/Users/19364/.ssh/known_hosts";

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            jsch.setKnownHosts(knownHostsPath);
            Session session = jsch.getSession(username, host, port);

            session.connect();
            System.out.println("Connected to " + host);

            // Execute a command
            String command = "ls -l";
            String result = executeCommand(session, command);
            System.out.println("Command output:\n" + result);

            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private static String executeCommand(Session session, String command) {
        StringBuilder output = new StringBuilder();
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();
            InputStream err = channel.getErrStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    output.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    break;
                }
                Thread.sleep(100);
            }

            while (err.available() > 0) {
                int i = err.read(tmp, 0, 1024);
                if (i < 0) break;
                output.append("ERROR: ").append(new String(tmp, 0, i));
            }

            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}