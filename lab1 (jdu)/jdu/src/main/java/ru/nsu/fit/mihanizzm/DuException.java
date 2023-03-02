package ru.nsu.fit.mihanizzm;

public class DuException extends RuntimeException {
    public DuException(String msg) {
        super(msg);
    }
}

class CommandLineArgsException extends DuException {
    public CommandLineArgsException(String msg) {
        super(msg);
    }
}