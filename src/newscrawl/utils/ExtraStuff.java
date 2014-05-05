package newscrawl.utils;

import jpt.FileWorker;

/**
 *
 * @author Akash
 */
public class ExtraStuff {
    FileWorker fileWorker;
    BaseURLManager baseURLManager;

    public void setFileWorker(FileWorker fileWorker) {
        this.fileWorker = fileWorker;
    }

    public void setBaseURLManager(BaseURLManager baseURLManager) {
        this.baseURLManager = baseURLManager;
    }

    public FileWorker getFileWorker() {
        return fileWorker;
    }

    public BaseURLManager getBaseURLManager() {
        return baseURLManager;
    }
    
}
