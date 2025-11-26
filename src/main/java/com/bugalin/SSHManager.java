package com.bugalin;

import com.jcraft.jsch.*;

import java.io.InputStream;

public class SSHManager {
    private String host;
    private String username;
    private int port;
    private String privateKeyPath;
    private String knownHostsPath;
    private int status; // -1 = Not yet initialized, 0 = Disconnected, 1 = Connected
    private Session session;

    public SSHManager(String host, String username, int port, String privateKeyPath, String knownHostsPath) {
        this.host = host;
        this.username = username;
        this.port = port;
        this.privateKeyPath = privateKeyPath;
        this.knownHostsPath = knownHostsPath;
        this.status = -1;
    }

    public SSHManager() {
        this.host = "10.129.240.97";
        this.username = "ubuntu";
        this.port = 22;
        this.privateKeyPath = "C:/Users/19364/.ssh/keyBugalin.pem";
        this.knownHostsPath = "C:/Users/19364/.ssh/known_hosts";
        this.status = -1;
    }

    public void setHost(String host) {
        if (status == 1){return;}
        this.host = host;
        status = -1;
    }

    public void setUsername(String username) {
        if (status == 1){return;}
        this.username = username;
        status = -1;
    }

    public void setPort(int port) {
        if (status == 1){return;}
        this.port = port;
        status = -1;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        if (status == 1){return;}
        this.privateKeyPath = privateKeyPath;
        status = -1;
    }

    public void setKnownHostsPath(String knownHostsPath) {
        if (status == 1){return;}
        this.knownHostsPath = knownHostsPath;
        status = -1;
    }

    public int getStatus() {
        return this.status;
    }

    public void initialize(){
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            jsch.setKnownHosts(knownHostsPath);
            this.session = jsch.getSession(username,host,port);
            this.status = 0;
            System.out.println("[SSH Connection] Initialized");
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        if (status != 0){return;}
        try {
            session.connect();
            this.status = 1;
            System.out.println("[SSH Connection] Session Connected");
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if (status != 1){return;}
        try {
            session.disconnect();
            this.status = 0;
            System.out.println("[SSH Connection] Session Disconnected");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ChannelSftp(){
        if (status != 1){return;}
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
        }
    }

    public void ChannelExec(){
        if (status != 1){return;}
        ChannelExec channel = null;
        StringBuilder output = new StringBuilder();
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls -a");

            InputStream in = channel.getInputStream();
            InputStream err = channel.getErrStream();

            channel.connect();

            byte[] buffer = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int bytesRead = in.read(buffer, 0, 1024);
                    if (bytesRead < 0) break;
                    output.append(new String(buffer, 0, bytesRead));
                }

                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    break;
                }
                Thread.sleep(100);
            }

            // Read error output
            while (err.available() > 0) {
                int bytesRead = err.read(buffer, 0, 1024);
                if (bytesRead < 0) break;
                output.append("ERROR: ").append(new String(buffer, 0, bytesRead));
            }

            System.out.println("Exit status: " + channel.getExitStatus());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        System.out.println(output.toString());
    }
}