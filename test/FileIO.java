package com.arithmetic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileIO {
    // 写入内容到文件（覆盖写入，若文件不存在则创建）
    public void fileWrite(String content, String filePath) throws IOException {
        // 确保父目录存在（如textFile文件夹）
        Files.createDirectories(Paths.get(filePath).getParent());
        // 写入内容（UTF-8编码）
        Files.write(Paths.get(filePath), content.getBytes("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // 读取文件内容（用于批改功能，读取题目/答案文件）
    public String fileRead(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes, "UTF-8");
    }
}