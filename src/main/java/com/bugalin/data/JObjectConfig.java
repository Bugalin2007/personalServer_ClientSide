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
        sshData.setHost("10.129.240.97");
        sshData.setUsername("ubuntu");
        sshData.setPort(22);
        sshData.setPrivateKeyPath("C:/Users/19364/.ssh/keyBugalin.pem");
        sshData.setKnownHostsPath("C:/Users/19364/.ssh/known_hosts");
        jObjectConfig.setSshData(sshData);
        return jObjectConfig;
    }
}
