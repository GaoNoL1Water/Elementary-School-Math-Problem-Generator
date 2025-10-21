package com.arithmetic;

public class Save {
    // 静态变量：保存所有中间结果（类共享，用于查重）
    public static String string = "";

    // 保存单次计算的中间结果
    public static void save(String s) {
        string += s;
    }
}