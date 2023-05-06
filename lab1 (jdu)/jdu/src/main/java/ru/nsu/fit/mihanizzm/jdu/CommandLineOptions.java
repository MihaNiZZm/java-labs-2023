package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Path;

public record CommandLineOptions(Path rootPath, int limit, int depth, boolean isCheckingSymLinks) {
    // CR: why not use default implementation?
    // CR: also you must always define equals + hashCode together
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CommandLineOptions)) {
            return false;
        }

        if (!this.rootPath.equals(((CommandLineOptions) obj).rootPath)) {
            return false;
        }
        if (this.limit != ((CommandLineOptions) obj).limit) {
            return false;
        }
        if (this.depth != ((CommandLineOptions) obj).depth) {
            return false;
        }
        if (this.isCheckingSymLinks != ((CommandLineOptions) obj).isCheckingSymLinks) {
            return false;
        }

        return true;
    }
}
