package com.arithmetic;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class Expression {
    private Random random = new Random();

    // 生成单个表达式（核心：处理除法、减法，确保结果合法，且运算符个数不超过3个）
    public String generateExp(Integer limit) {
        // 随机生成运算符个数（1-3个）
        int opCount = random.nextInt(3) + 1;
        StringBuilder expr = new StringBuilder();

        // 生成第一个操作数（确保开头为非负操作数）
        String firstOperand = generateOperand(limit);
        expr.append(firstOperand);

        // 循环生成运算符+操作数（循环次数为opCount，确保运算符个数不超过3个）
        for (int i = 0; i < opCount; i++) {
            char op = generateOperator();

            // ---------- 除法特殊处理：确保 e1 ÷ e2 结果为真分数 ----------
            if (op == '/') {
                // 1. 生成第二个操作数 e2
                String e2Str = generateOperand(limit);
                Fraction e2 = parseOperandToFraction(e2Str);

                // 2. 生成**真分数**作为除法结果（分子 < 分母）
                Fraction divisionResult = generateTrueFraction(limit);

                // 3. 反推 e1 = e2 × 除法结果（确保 e1 ÷ e2 = 真分数）
                Fraction e1 = e2.muti(divisionResult); // 修正：muti → multiply

                // 4. 检查 e1 是否在 limit 范围内
                if (!isOperandInRange(e1, limit)) {
                    i--; // 回退循环，重新生成
                    continue;
                }

                // 5. 拼接除法表达式
                expr.setLength(0); // 清空之前的操作数
                expr.append(convertFractionToOperand(e1)).append(" / ").append(e2Str);
            }
            // ---------- 减法特殊处理：确保 e1 - e2 结果非负（e1 ≥ e2） ----------
            else if (op == '-') {
                // 1. 生成第二个操作数 e2
                String e2Str = generateOperand(limit);
                Fraction e2 = parseOperandToFraction(e2Str);

                // 2. 生成**非负结果**（0 或正分数）
                Fraction subtractionResult = generateNonNegativeFraction(limit);

                // 3. 反推 e1 = e2 + 非负结果（确保 e1 ≥ e2）
                Fraction e1 = e2.add(subtractionResult);

                // 4. 检查 e1 是否在 limit 范围内
                if (!isOperandInRange(e1, limit)) {
                    i--; // 回退循环，重新生成
                    continue;
                }

                // 5. 拼接减法表达式
                expr.setLength(0);
                expr.append(convertFractionToOperand(e1)).append(" - ").append(e2Str);
            }
            // ---------- 加法、乘法：正常生成操作数 ----------
            else {
                expr.append(" ").append(op).append(" ");
                expr.append(generateOperand(limit));
            }

            // 随机添加括号（仅对前两个操作数，概率 50%）
            if (i == 0 && opCount > 1 && random.nextBoolean()) {
                expr.insert(0, "(").append(")");
            }
        }
        return expr.toString();
    }

    // 辅助：生成操作数（自然数、真分数、带分数，均非负）
    private String generateOperand(int limit) {
        if (random.nextBoolean()) {
            // 生成自然数（0 到 limit-1，天然非负）
            return String.valueOf(random.nextInt(limit));
        } else {
            // 生成真分数（分母 2 到 limit-1，分子 1 到分母-1，均正）
            int denominator = random.nextInt(limit - 2) + 2;
            int numerator = random.nextInt(denominator - 1) + 1;
            // 30% 概率生成带分数（整数部分 1 到 limit-1，正）
            if (random.nextDouble() < 0.3) {
                int integer = random.nextInt(limit - 1) + 1;
                return integer + "'" + numerator + "/" + denominator;
            }
            return numerator + "/" + denominator;
        }
    }

    // 辅助：生成运算符（+、-、*、/）
    private char generateOperator() {
        char[] ops = {'+', '-', '*', '/'};
        return ops[random.nextInt(ops.length)];
    }

    // 生成真分数（分子 < 分母，分母在 limit 范围内）
    private Fraction generateTrueFraction(int limit) {
        int denominator = random.nextInt(limit - 2) + 2;
        int numerator = random.nextInt(denominator - 1) + 1;
        Fraction f = new Fraction(numerator, denominator);
        f.Appointment(); // 约分
        return f;
    }

    // 生成非负分数（0 或正分数，用于减法结果）
    private Fraction generateNonNegativeFraction(int limit) {
        if (random.nextBoolean()) {
            return new Fraction(0); // 0（非负）
        } else {
            return generateTrueFraction(limit); // 正真分数
        }
    }

    // 将操作数字符串（如 "3" "1/2" "2'3/4"）转换为 Fraction 对象
    private Fraction parseOperandToFraction(String operand) {
        if (operand.contains("'")) {
            // 处理带分数（如 "2'3/4" → 2 + 3/4 = 11/4）
            String[] parts = operand.split("'");
            int integer = Integer.parseInt(parts[0]);
            String[] fracParts = parts[1].split("/");
            int numerator = Integer.parseInt(fracParts[0]);
            int denominator = Integer.parseInt(fracParts[1]);
            return new Fraction(integer * denominator + numerator, denominator);
        } else if (operand.contains("/")) {
            // 处理纯分数（如 "3/5"）
            String[] parts = operand.split("/");
            int numerator = Integer.parseInt(parts[0]);
            int denominator = Integer.parseInt(parts[1]);
            return new Fraction(numerator, denominator);
        } else {
            // 处理自然数（如 "5" → 5/1）
            int num = Integer.parseInt(operand);
            return new Fraction(num, 1);
        }
    }

    // 将 Fraction 对象转换为操作数字符串（带分数/真分数/自然数）
    private String convertFractionToOperand(Fraction f) {
        f.Appointment(); // 先约分
        int numerator = f.getNumerator();
        int denominator = f.getDenominator();

        if (denominator == 1) {
            return String.valueOf(numerator); // 自然数
        }
        if (numerator < denominator) {
            return numerator + "/" + denominator; // 真分数
        } else {
            // 带分数（整数部分 + 余数/分母）
            int integer = numerator / denominator;
            int remainder = numerator % denominator;
            return integer + "'" + remainder + "/" + denominator;
        }
    }

    // 检查 Fraction 是否在 limit 范围内（避免操作数越界）
    private boolean isOperandInRange(Fraction f, int limit) {
        f.Appointment();
        int numerator = f.getNumerator();
        int denominator = f.getDenominator();

        // 1. 分母不能超过 limit-1
        if (denominator >= limit) {
            return false;
        }
        // 2. 若为整数（分母=1），数值不能超过 limit-1
        if (denominator == 1 && numerator >= limit) {
            return false;
        }
        // 3. 若为带分数，整数部分不能超过 limit-1
        if (numerator >= denominator) {
            int integer = numerator / denominator;
            if (integer >= limit) {
                return false;
            }
        }
        return true;
    }

    // 生成合法且不重复的题目
    public void legalExp(Integer number, Integer limit) throws IOException {
        int j = 1;
        String str1; // 原始表达式
        String str2; // 化简后的答案
        StringBuilder str3 = new StringBuilder(); // 题目内容
        StringBuilder str4 = new StringBuilder(); // 答案内容
        HashMap<String, Integer> answers = new HashMap<>(); // 查重：key=中间结果

        FileIO writer = new FileIO();
        Calculate cal = new Calculate();

        do {
            str1 = generateExp(limit); // 生成表达式（无等号）
            Fraction f = cal.outcome(str1 + " = "); // 计算结果

            // 剔除非法表达式（如减法/除法的非法情况，标记为100000）
            if (f.getNumerator() == 100000) {
                continue;
            }

            str2 = f.transferFraction(); // 转换为带分数格式
            String middleResult = Save.string; // 获取计算中间结果（用于查重）

            // 查重：中间结果不存在才保留
            if (answers.containsKey(middleResult)) {
                continue;
            }
            answers.put(middleResult, null);

            // 格式化题目和答案
            str3.append(j).append(". ").append(str1).append(" = \n");
            str4.append(j).append(". ").append(str2).append("\n");
            System.out.printf("NO.%4d  %s = %n", j, str1);
            j++;

        } while (j <= number);

        System.out.println("表达式生成完毕");
        // 写入文件（路径：项目根目录/textFile/）
        String exprPath = "textFile/Expression.txt";
        String answerPath = "textFile/Answer.txt";
        writer.fileWrite(str3.toString(), exprPath);
        writer.fileWrite(str4.toString(), answerPath);
    }
}