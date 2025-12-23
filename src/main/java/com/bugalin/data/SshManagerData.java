package com.bugalin.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SshManagerData {
    private String host;
    private String username;
    private int port;
    private String privateKeyPath;
    private String knownHostsPath;

    public SshManagerData(){}

    @JsonIgnore
    public static SshManagerData getdefault() {
        SshManagerData sshManagerData = new SshManagerData();
        sshManagerData.host = "10.129.240.97";
        sshManagerData.username = "ubuntu";
        sshManagerData.port = 22;
        sshManagerData.privateKeyPath = "C:/Users/19364/.ssh/keyBugalin.pem";
        sshManagerData.knownHostsPath = "C:/Users/19364/.ssh/known_hosts";
        return sshManagerData;
    }

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
