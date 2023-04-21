package ru.nsu.fit.mihanizzm.jdu;

public class DuException extends RuntimeException {
    public DuException(String msg) {
        super(msg);
    }
}

class PrinterException extends DuException {
    public PrinterException(String msg) {
        super(msg);
    }
}

class CommandLineArgumentsException extends DuException {
    public CommandLineArgumentsException(String msg) {
        super(msg);
    }
}

class TreeBuilderException extends DuException {
    public TreeBuilderException(String msg) {
        super(msg);
    }
}