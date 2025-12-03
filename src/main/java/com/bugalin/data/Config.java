package com.bugalin.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Config {
    private SshManagerData sshManagerData;
    private RemoteFileHandlerData remoteFileHandlerData;

    public Config(){}

    public SshManagerData getSshData() {
        return sshManagerData;
    }
    public void setSshData(SshManagerData sshManagerData) {
        this.sshManagerData = sshManagerData;
    }
    public RemoteFileHandlerData getFileHandlerData() {return remoteFileHandlerData;}
    public void setFileHandlerData(RemoteFileHandlerData remoteFileHandlerData) {this.remoteFileHandlerData = remoteFileHandlerData;}

    @JsonIgnore
    public static Config defaultConfig(){
        Config jObjectConfig = new Config();

        SshManagerData sshManagerData = new SshManagerData();
        sshManagerData.setHost("10.129.240.97");
        sshManagerData.setUsername("ubuntu");
        sshManagerData.setPort(22);
        sshManagerData.setPrivateKeyPath("C:/Users/19364/.ssh/keyBugalin.pem");
        sshManagerData.setKnownHostsPath("C:/Users/19364/.ssh/known_hosts");
        jObjectConfig.setSshData(sshManagerData);

        RemoteFileHandlerData remoteFileHandlerData = new RemoteFileHandlerData();
        remoteFileHandlerData.setCurrentDir("/");
        jObjectConfig.setFileHandlerData(remoteFileHandlerData);

        return jObjectConfig;
    }
}
