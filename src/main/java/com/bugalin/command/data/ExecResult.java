package com.bugalin.command.data;

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
}
