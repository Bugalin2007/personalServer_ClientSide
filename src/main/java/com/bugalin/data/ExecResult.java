package com.bugalin.data;

public record ExecResult(ExitStatus exitStatus, String output, String error) {

    public ExecResult(int exitCode, String str){
        this(ExitStatus.parseExitCode(exitCode), exitCode == 0 ? str : null, exitCode == 0 ? null : str);
    }

    public boolean isSuccess() {
        return exitStatus.isSuccess();
    }

    public boolean isExit(){
        return exitStatus == ExitStatus.EXIT_PROGRAM;
    }

    public boolean isCommandUsageProblem() {
        return exitStatus == ExitStatus.UNKNOWN_COMMAND || exitStatus == ExitStatus.INVALID_ARGUMENT;
    }
}
