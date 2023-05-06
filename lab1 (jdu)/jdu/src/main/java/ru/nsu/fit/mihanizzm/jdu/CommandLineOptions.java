package ru.nsu.fit.mihanizzm.jdu;

import java.nio.file.Path;

public record CommandLineOptions(Path rootPath, int limit, int depth, boolean isCheckingSymLinks) {
    // CR: why not use default implementation?
    // Didn't work in tests for some reason.
    // CR: also you must always define equals + hashCode together
    // Added hashCode.
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
        return this.isCheckingSymLinks == ((CommandLineOptions) obj).isCheckingSymLinks;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result += rootPath.toString().hashCode();
        result += 31 * limit;
        result += 31 * depth;

        if (isCheckingSymLinks) {
            result += 31 * 31;
        } else {
            result += 31;
        }

        return result;
    }
}
