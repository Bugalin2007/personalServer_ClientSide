package com.bugalin.data;

public class ExecResult {
    private final int exitStatus;
    private final String output;
    private final String error;

    public ExecResult(int exitStatus, String output, String error) {
        this.exitStatus = exitStatus;
        this.output = output;
        this.error = error;
    }

    public boolean isSuccess() { return exitStatus == 0; }
    public int getExitStatus() { return exitStatus; }
    public String getOutput() { return output; }
    public String getError() { return error; }
}
