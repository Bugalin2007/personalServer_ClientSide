package com.bugalin.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JObjectConfig {
    private SshData sshData;

    public JObjectConfig(){}

    public SshData getSshData() {
        return sshData;
    }
    public void setSshData(SshData sshData) {
        this.sshData = sshData;
    }

    @JsonIgnore
    public static JObjectConfig defaultConfig(){
        JObjectConfig jObjectConfig = new JObjectConfig();
        SshData sshData = new SshData();
        sshData.setHost("127.0.0.1");
        sshData.setUsername("linux");
        sshData.setPort(0);
        sshData.setPrivateKeyPath("C:/Users/username/.ssh/key.pem");
        sshData.setKnownHostsPath("C:/Users/username/.ssh/known_hosts");
        jObjectConfig.setSshData(sshData);
        return jObjectConfig;
    }
}
