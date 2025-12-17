package com.bugalin.handler;

import com.bugalin.data.ExitStatus;
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
            return;
        }
        this.host = sshManagerData.getHost();
        this.username = sshManagerData.getUsername();
        this.port = sshManagerData.getPort();
        this.privateKeyPath = sshManagerData.getPrivateKeyPath();
        this.knownHostsPath = sshManagerData.getKnownHostsPath();
        this.status = -1;
    }

    public int getStatusCode() {
        return this.status;
    }

    public String getStatus(){
        switch (this.status){
            case 0 -> {return "Session is currently disconnected";}
            case 1 -> {return "Session is currently connected. ";}
            case -1 -> {return "Session not exist. Initialization required. ";}
            default -> {return "Unknown session status";}
        }
    }

    public ExecResult initialize(){
        if (status == 1){
            disconnect();
        }
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            jsch.setKnownHosts(knownHostsPath);
            this.session = jsch.getSession(username,host,port);
            this.status = 0;
            return new ExecResult(ExitStatus.SUCCESS,null,"Jsch initialized.");
        } catch (JSchException e) {
            return new ExecResult(ExitStatus.CONNECTION_ERROR,null,"Cannot initialize Jsch. \n   Detailed description: "+e.getMessage());
        }
    }

    public ExecResult connect(){
        if (status != 0){
            return new ExecResult(ExitStatus.INVALID_OPERATION,null,"Cannot connect when connected.");
        }
        try {
            session.connect();
            this.status = 1;
            return new ExecResult(ExitStatus.SUCCESS,null,"Connected to server.");
        } catch (JSchException e) {
            return new ExecResult(ExitStatus.CONNECTION_ERROR,null,"Cannot connect to server. \n   Detailed description: "+e.getMessage());
        }
    }

    public ExecResult disconnect(){
        if (status != 1){
            return new ExecResult(ExitStatus.INVALID_OPERATION,null,"Cannot disconnect when not connected.");
        }
        try {
            session.disconnect();
            this.status = 0;
            return new ExecResult(ExitStatus.SUCCESS,null,"Disconnected from server.");
        }catch (Exception e){
            return new ExecResult(ExitStatus.CONNECTION_ERROR,null,"Cannot disconnect from server. \n   Detailed description: "+e.getMessage());
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

    public ExecResult executeCommand(String command){
        return ChannelExec(command);
    }

    public ExecResult executeCommandAtDir(String command, String directory){
        return ChannelExec("cd "+directory+" && "+command);
    }

    private ExecResult ChannelExec(String command){
        if (status != 1){return new ExecResult(ExitStatus.UNKNOWN_ERROR,null,"There is no active session.");}
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

            while (err.available() > 0) {
                int bytesRead = err.read(buffer, 0, 1024);
                if (bytesRead < 0) break;
                error.append(new String(buffer, 0, bytesRead));
            }

            return new ExecResult(ExitStatus.parseExitCode(channel.getExitStatus()), output.toString(),error.toString());

        } catch (Exception e) {
            return new ExecResult(ExitStatus.UNKNOWN_ERROR,null,e.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}