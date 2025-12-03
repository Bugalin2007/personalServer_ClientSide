package com.bugalin.data;

public class SshManagerData {
    private String host;
    private String username;
    private int port;
    private String privateKeyPath;
    private String knownHostsPath;

    public SshManagerData(){}

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }
    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }
    public String getKnownHostsPath() {
        return knownHostsPath;
    }
    public void setKnownHostsPath(String knownHostsPath) {
        this.knownHostsPath = knownHostsPath;
    }
}
