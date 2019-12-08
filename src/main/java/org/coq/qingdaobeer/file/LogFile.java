package org.coq.qingdaobeer.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogFile extends RandomAccessFile {
    public LogFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public LogFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    public void appendSuccess(String phone, String msg) throws IOException {
        write((phone + "成功抽奖：" + msg + "\r\n").getBytes());
    }

    public void appendFailed(String phone, String msg) throws IOException {
        write((phone + "抽奖失败：" + msg + "\r\n").getBytes());
    }
}
