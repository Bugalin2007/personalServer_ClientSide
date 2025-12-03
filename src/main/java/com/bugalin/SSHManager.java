package com.bugalin;

import com.bugalin.data.ExecResult;
import com.bugalin.data.SshManagerData;
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

    public SSHManager(SshManagerData sshManagerData) {
        this.host = sshManagerData.getHost();
        this.username = sshManagerData.getUsername();
        this.port = sshManagerData.getPort();
        this.privateKeyPath = sshManagerData.getPrivateKeyPath();
        this.knownHostsPath = sshManagerData.getKnownHostsPath();
        this.status = -1;
    }

    public void setData(SshManagerData sshManagerData) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.host = sshManagerData.getHost();
        this.username = sshManagerData.getUsername();
        this.port = sshManagerData.getPort();
        this.privateKeyPath = sshManagerData.getPrivateKeyPath();
        this.knownHostsPath = sshManagerData.getKnownHostsPath();
        this.status = -1;
    }

    public void setHost(String host) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.host = host;
        status = -1;
    }

    public void setUsername(String username) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.username = username;
        status = -1;
    }

    public void setPort(int port) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.port = port;
        status = -1;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.privateKeyPath = privateKeyPath;
        status = -1;
    }

    public void setKnownHostsPath(String knownHostsPath) {
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, data modify is forbidden.");
            return;
        }
        this.knownHostsPath = knownHostsPath;
        status = -1;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatus(boolean isNumber){
        if (isNumber){
            return String.valueOf(getStatus());
        }
        switch (this.status){
            case 0 -> {return "[SSH Connection] Session is currently disconnected";}
            case 1 -> {return "[SSH Connection] Session is currently connected. ";}
            case -1 -> {return "[SSH Connection] Session not exist. Initialization required. ";}
            default -> {return "[SSH Connection] Unknown session status";}
        }
    }

    public int initialize(){
        if (status == 1){
            System.out.println("[SSH Connection] Existing active session, initialization is forbidden.");
            return 1;
        }
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            jsch.setKnownHosts(knownHostsPath);
            this.session = jsch.getSession(username,host,port);
            this.status = 0;
            System.out.println("[SSH Connection] Initialized");
            return 0;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public int connect(){
        if (status != 0){
            System.out.println("[SSH Connection] Cannot perform session connect.");
            return 1;
        }
        try {
            session.connect();
            this.status = 1;
            System.out.println("[SSH Connection] Session Connected");
            return 0;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public int disconnect(){
        if (status != 1){
            System.out.println("[SSH Connection] Cannot perform session disconnect.");
            return 1;
        }
        try {
            session.disconnect();
            this.status = 0;
            System.out.println("[SSH Connection] Session Disconnected");
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 2;
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

    public ExecResult ChannelExec(String command){
        if (status != 1){return new ExecResult(-1,"","There is no active session.");}
        ChannelExec channel = null;
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

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
                error.append("ERROR: ").append(new String(buffer, 0, bytesRead));
            }

            return new ExecResult(channel.getExitStatus(), output.toString(), error.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        return new ExecResult(-1,"","Unknown Error");
    }
}