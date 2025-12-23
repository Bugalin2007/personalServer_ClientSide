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

    public ExecResult executeCommand(String command){
        return ChannelExec(command);
    }

    public ExecResult executeCommandAtDir(String command, String directory){
        return ChannelExec("cd "+directory+" && "+command);
    }

    public ExecResult uploadFile(String fileName, String localPath, String remotePath){
        return ChannelSftp(fileName,localPath,remotePath,true);
    }

    public ExecResult downloadFile(String fileName, String localPath, String remotePath){
        return ChannelSftp(fileName,localPath,remotePath,false);
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

    private ExecResult ChannelSftp(String fileName, String localPath, String remotePath, boolean isUpload){
        ChannelSftp channel = null;
        String action = isUpload ? "upload" : "download";
        try{
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.cd("/");
            SftpProgressMonitor monitor = createProgressMonitor(fileName, action);
            if(isUpload){
                channel.put(localPath, remotePath,monitor);
            }else{
                channel.get(remotePath, localPath,monitor);
            }
            return new ExecResult(ExitStatus.SUCCESS,action+" complete.",null);
        } catch (JSchException | SftpException e) {
            return new ExecResult(ExitStatus.UNKNOWN_ERROR,null,e.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    private SftpProgressMonitor createProgressMonitor(String fileName, String operation) {
        return new SftpProgressMonitor() {
            private long totalSize;
            private long transferred = 0;
            private int lastProgress = -1;

            @Override
            public void init(int op, String src, String dest, long max) {
                this.totalSize = max;
                System.out.println(operation + " " + fileName + " :begin with file size " + formatFileSize(max));
            }

            @Override
            public boolean count(long count) {
                transferred += count;
                int progress = (int) ((transferred * 100) / totalSize);

                if (progress != lastProgress && progress % 5 == 0) {
                    System.out.println(operation + " " + fileName + ": " + progress + "% (" +
                            formatFileSize(transferred) + "/" + formatFileSize(totalSize) + ")");
                    lastProgress = progress;
                }
                return true;
            }

            @Override
            public void end() {
                System.out.println(operation + " " + fileName + " :finished with file size " + formatFileSize(transferred));
            }

            private String formatFileSize(long size) {
                if (size < 1024) {
                    return size + " B";
                } else if (size < 1024 * 1024) {
                    return String.format("%.1f KB", size / 1024.0);
                } else if (size < 1024 * 1024 * 1024) {
                    return String.format("%.1f MB", size / (1024.0 * 1024.0));
                } else {
                    return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
                }
            }
        };
    }
}