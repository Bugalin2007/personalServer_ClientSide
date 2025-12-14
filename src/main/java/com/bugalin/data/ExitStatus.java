package com.bugalin.data;

public enum ExitStatus {
    SUCCESS(0),
    EXIT_PROGRAM(-1),
    UNKNOWN_ERROR(-2),
    // Command Parse Error
    EMPTY_INPUT(20),
    UNKNOWN_COMMAND(21),
    INVALID_ARGUMENT(22),;

    private final int code;
    ExitStatus(int code) {
        this.code = code;
    }

    public static ExitStatus parseExitCode(int exitCode) {
        return exitCode == 0 ? SUCCESS : UNKNOWN_ERROR;
    }

    public int code() {
        return code;
    }
    public boolean isSuccess() {
        return this == SUCCESS || this == EXIT_PROGRAM || this == EMPTY_INPUT;
    }
}
