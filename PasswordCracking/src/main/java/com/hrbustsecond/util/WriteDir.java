package com.hrbustsecond.util;

import java.io.File;
import java.util.List;

public class WriteDir extends Thread {
    private File file;
    private List<String> dirResult;

    public WriteDir(File file,List<String> dirResult){
        file = this.file;
        dirResult = this.dirResult;
    }

    @Override
    public void run() {

    }
}
