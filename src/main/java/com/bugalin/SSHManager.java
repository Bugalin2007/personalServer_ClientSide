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
    private JSch jsch;
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
    }

    public void setUsername(String username) {
        if (status == 1){return;}
        this.username = username;
    }

    public void setPort(int port) {
        if (status == 1){return;}
        this.port = port;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        if (status == 1){return;}
        this.privateKeyPath = privateKeyPath;
    }

    public void setKnownHostsPath(String knownHostsPath) {
        if (status == 1){return;}
        this.knownHostsPath = knownHostsPath;
    }

    public int getStatus() {
        return this.status;
    }

    public void initialize(){
        try {
            this.jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            jsch.setKnownHosts(knownHostsPath);
            this.session = jsch.getSession(host,username,port);
            this.status = 0;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            session.connect();
            this.status = 1;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try {
            session.disconnect();
            this.status = 0;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}