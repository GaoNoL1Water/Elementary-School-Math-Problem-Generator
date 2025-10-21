package com.arithmetic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Grade {
    public void grade(String exercisePath, String answerPath) throws IOException {
        int j = 1;
        String exprLine; // 读取的题目行（如“1. 3 + 2 = ”）
        String answerLine; // 读取的答案行（如“1. 5”）
        StringBuilder gradeResult = new StringBuilder(); // 批改结果

        Queue<Integer> correctQueue = new LinkedList<>();
        Queue<Integer> wrongQueue = new LinkedList<>();

        // 读取题目文件和答案文件
        BufferedReader exprReader = new BufferedReader(new InputStreamReader(new FileInputStream(exercisePath)));
        BufferedReader answerReader = new BufferedReader(new InputStreamReader(new FileInputStream(answerPath)));
        Scanner scanner = new Scanner(System.in);

        System.out.println("答题开始（输入'quit'结束）：");

        while ((exprLine = exprReader.readLine()) != null && (answerLine = answerReader.readLine()) != null) {
            // 1. 提取纯表达式（去掉题号和“=”，如“1. 3 + 2 = ” → “3 + 2”）
            String cleanExpr = exprLine.replaceAll("^\\d+\\. | = $", "").trim();
            // 2. 提取正确答案（去掉题号，如“1. 5” → “5”）
            String correctAnswer = answerLine.replaceAll("^\\d+\\. ", "").trim();

            System.out.println("\n" + j + ". " + cleanExpr + " = ");
            System.out.print("你的答案：");
            String userAnswer = scanner.next();

            // 处理退出指令
            if (userAnswer.equalsIgnoreCase("quit")) {
                System.out.println("答题中止，未答题目计为错误");
                wrongQueue.add(j);
                // 剩余题目全部计为错误
                while ((exprLine = exprReader.readLine()) != null) {
                    j++;
                    wrongQueue.add(j);
                }
                break;
            }

            // 计算题目正确结果
            Calculate cal = new Calculate();
            Fraction result = cal.outcome(cleanExpr + " = ");
            String realAnswer = result.transferFraction();

            // 比对答案
            if (userAnswer.equals(realAnswer)) {
                System.out.println("正确！");
                correctQueue.add(j);
            } else {
                System.out.println("错误，正确答案：" + realAnswer);
                wrongQueue.add(j);
            }
            j++;
        }

        // 统计结果
        int correctCount = correctQueue.size();
        int wrongCount = wrongQueue.size();
        double accuracy = (correctCount + wrongCount) == 0 ? 0 : (double) correctCount / (correctCount + wrongCount) * 100;

        // 生成批改报告
        gradeResult.append("Correct: ").append(correctCount).append(" ").append(correctQueue).append("\n");
        gradeResult.append("Wrong: ").append(wrongCount).append(" ").append(wrongQueue).append("\n");
        gradeResult.append("Accuracy: ").append(String.format("%.1f", accuracy)).append("%");

        // 写入Grade.txt
        FileIO writer = new FileIO();
        writer.fileWrite(gradeResult.toString(), "textFile/Grade.txt");

        // 控制台打印结果
        System.out.println("\n===== 答题结果 =====");
        System.out.println("正确：" + correctCount + " 道 " + correctQueue);
        System.out.println("错误：" + wrongCount + " 道 " + wrongQueue);
        System.out.println("正确率：" + String.format("%.1f", accuracy) + "%");

        // 关闭流
        exprReader.close();
        answerReader.close();
        scanner.close();
    }
}