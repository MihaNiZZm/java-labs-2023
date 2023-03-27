package ru.nsu.fit.mihanizzm;

import java.io.File;

public class DuException extends RuntimeException {
    public DuException(String msg) {
        super(msg);
    }
}

class CommandLineException extends DuException {
    public CommandLineException(String msg) {
        super(msg);
    }
}

class ArgumentException extends CommandLineException {
    public ArgumentException(String msg) {
        super(msg);
    }
}

class UnknownArgumentException extends ArgumentException {
    public UnknownArgumentException(String msg) {
        super(msg);
    }
}

class ArgumentValueException extends ArgumentException {
    public ArgumentValueException(String msg) {
        super(msg);
    }
}

class NoArgumentValueException extends ArgumentValueException {
    public NoArgumentValueException(String msg) {
        super(msg);
    }
}

class InvalidArgumentValueException extends ArgumentValueException {
    public InvalidArgumentValueException(String msg) {
        super(msg);
    }
}

class ArgumentNumericalException extends ArgumentException {
    public ArgumentNumericalException(String msg) {
        super(msg);
    }
}

class FileException extends DuException {
    public FileException(String msg) {
        super(msg);
    }
}

class FileAnalysisException extends FileException {
    public FileAnalysisException(String msg) {
        super(msg);
    }
}

class UnknownFileException extends FileException {
    public UnknownFileException(String msg) {
        super(msg);
    }
}