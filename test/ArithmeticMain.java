package com.arithmetic;

import java.io.IOException;

public class ArithmeticMain {
    public static void main(String[] args) {
        // 解析命令行参数
        if (args.length == 0) {
            printHelp();
            return;
        }

        try {
            // 情况1：生成题目（参数格式：-n 数量 -r 范围）
            if (args[0].equals("-n") && args[2].equals("-r")) {
                int num = Integer.parseInt(args[1]);
                int range = Integer.parseInt(args[3]);
                if (range < 1) {
                    System.out.println("错误：-r 参数必须为自然数");
                    return;
                }
                Expression exprGenerator = new Expression();
                exprGenerator.legalExp(num, range);
            }
            // 情况2：批改题目（参数格式：-e 题目文件 -a 答案文件）
            else if (args[0].equals("-e") && args[2].equals("-a")) {
                String exerciseFile = args[1];
                String answerFile = args[3];
                Grade grader = new Grade();
                grader.grade(exerciseFile, answerFile);
            }
            // 无效参数
            else {
                printHelp();
            }
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("参数格式错误！");
            printHelp();
        } catch (IOException e) {
            System.out.println("文件操作错误：" + e.getMessage());
        }
    }

    // 帮助信息：提示正确的参数格式
    private static void printHelp() {
        System.out.println("小学四则运算程序 - 使用说明：");
        System.out.println("1. 生成题目：java -cp [class路径] com.arithmetic.ArithmeticMain -n 题目数量 -r 数值范围");
        System.out.println("   示例：java com.arithmetic.ArithmeticMain -n 10 -r 5 （生成10道5以内题目）");
        System.out.println("2. 批改题目：java com.arithmetic.ArithmeticMain -e 题目文件路径 -a 答案文件路径");
        System.out.println("   示例：java com.arithmetic.ArithmeticMain -e textFile/Expression.txt -a textFile/Answer.txt");
        System.out.println("注意：-r 参数必须为自然数，生成的题目/答案保存在 textFile 文件夹下");
    }
}