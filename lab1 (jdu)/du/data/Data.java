package du.data;

import java.nio.file.Path;
import java.nio.file.Paths;

public record Data(Boolean isHaveDepthKey, Boolean isHaveLimitKey, Boolean isCheckingSymLinks, Boolean isHaveCustomFilePath, int depthSize, int limitSize, Path rootFilePath) { 
    static final int DEFAULT_DEPTH_SIZE = -1;
    static final int DEFAULT_LIMIT_SIZE = -1;

    public Data(Boolean isHaveDepthKey, Boolean isHaveLimitKey, Boolean isCheckingSymLinks, Boolean isHaveCustomFilePath, int depthSize, int limitSize, Path rootFilePath) { 
        this.isHaveDepthKey = isHaveDepthKey;
        this.isHaveLimitKey = isHaveLimitKey;
        this.isCheckingSymLinks = isCheckingSymLinks;
        this.isHaveCustomFilePath = isHaveCustomFilePath;

        if (this.isHaveCustomFilePath) {
            this.rootFilePath = rootFilePath;
        }
        else  {
            this.rootFilePath = (Paths.get("")).toAbsolutePath();
        }

        if (this.isHaveDepthKey) {
            this.depthSize = depthSize;
        }
        else {
            this.depthSize = DEFAULT_DEPTH_SIZE;
        }

        if (this.isHaveLimitKey) {
            this.limitSize = limitSize;
        }
        else {
            this.limitSize = DEFAULT_LIMIT_SIZE;
        }
    }
}
