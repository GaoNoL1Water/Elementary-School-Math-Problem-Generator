package com.arithmetic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calculate {
    // 3.1节原有代码：计算后缀表达式
    public Fraction calculate(Queue<String> queue) {
        Stack<Fraction> fracStack = new Stack<>();
        String str = "";
        Save.string = ""; // 重置中间结果

        while (!queue.isEmpty()) {
            String s = queue.remove();
            // 判断是否为数字（补充isDigital方法）
            if (isDigital(s.charAt(0))) {
                Fraction f = new Fraction(Integer.parseInt(s));
                fracStack.push(f);
            }
            // 判断是否为运算符（补充isOperator方法）
            else if (isOperator(s.charAt(0))) {
                char c = s.charAt(0);
                // 弹出两个操作数（注意顺序：先弹右操作数，再弹左操作数）
                if (fracStack.size() < 2) {
                    fracStack.push(new Fraction(100000)); // 非法表达式标记
                    break;
                }
                Fraction f1 = fracStack.pop(); // 右操作数
                Fraction f2 = fracStack.pop(); // 左操作数
                Fraction result = new Fraction(0);

                // 合法性校验
                if (c == '-') {
                    result = f2.sub(f1);
                    // 减法结果不能为负
                    if (result.getNumerator() < 0) {
                        fracStack.push(new Fraction(100000));
                        break;
                    }
                }
                if (c == '/') {
                    // 除数不能为0
                    if (f1.getNumerator() == 0) {
                        fracStack.push(new Fraction(100000));
                        break;
                    }
                }

                // 四则运算
                switch (c) {
                    case '+':
                        result = f2.add(f1);
                        break;
                    case '-':
                        result = f2.sub(f1);
                        break;
                    case '*':
                        result = f2.muti(f1);
                        break;
                    case '/':
                        result = f2.div(f1);
                        break;
                }
                fracStack.push(result);
                // 记录中间结果（用于查重）
                str += result.getNumerator() + "/" + result.getDenominator() + " ";
            }
        }

        Save.save(str); // 保存中间结果
        // 判断是否非法（分子为100000）
        if (fracStack.isEmpty() || fracStack.peek().getNumerator() == 100000) {
            return new Fraction(100000);
        } else {
            return fracStack.pop();
        }
    }

    // ---------------------- 补充缺失方法 ----------------------
    // 1. 判断是否为数字（0-9）
    private boolean isDigital(char c) {
        return c >= '0' && c <= '9';
    }

    // 2. 判断是否为运算符（+、-、×、÷，注意代码中是否用×还是*，建议统一用*）
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // 3. 新增：将表达式字符串转为后缀表达式队列（如"3 + 2 * 4" → [3,2,4,*,+]）
    public Fraction outcome(String expr) {
        // 1. 清理表达式：去掉"="和题号（如"1.四则运算题目1    3 + 2 = " → "3 + 2"）
        String cleanExpr = expr.replaceAll("=|\\d+\\.|四则运算题目\\d+", "").trim();
        // 2. 中缀转后缀（核心逻辑）
        Queue<String> postfixQueue = infixToPostfix(cleanExpr);
        // 3. 计算后缀表达式
        return calculate(postfixQueue);
    }

    // 辅助：中缀表达式转后缀表达式
    private Queue<String> infixToPostfix(String expr) {
        Queue<String> postfix = new LinkedList<>();
        Stack<Character> opStack = new Stack<>();
        StringBuilder numSb = new StringBuilder(); // 拼接多位数（如12、34）

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') continue; // 跳过空格

            if (isDigital(c)) {
                numSb.append(c); // 拼接数字
                // 判断是否为最后一位，或下一位非数字（结束数字拼接）
                if (i == expr.length() - 1 || !isDigital(expr.charAt(i + 1))) {
                    postfix.offer(numSb.toString());
                    numSb.setLength(0); // 重置
                }
            } else if (c == '(') {
                opStack.push(c);
            } else if (c == ')') {
                // 弹出栈中运算符，直到遇到"("
                while (!opStack.isEmpty() && opStack.peek() != '(') {
                    postfix.offer(String.valueOf(opStack.pop()));
                }
                opStack.pop(); // 弹出"("（不加入队列）
            } else if (isOperator(c)) {
                // 弹出优先级高于/等于当前运算符的栈顶元素
                while (!opStack.isEmpty() && getOpPriority(opStack.peek()) >= getOpPriority(c)) {
                    postfix.offer(String.valueOf(opStack.pop()));
                }
                opStack.push(c);
            }
        }

        // 弹出栈中剩余运算符
        while (!opStack.isEmpty()) {
            postfix.offer(String.valueOf(opStack.pop()));
        }
        return postfix;
    }

    // 辅助：定义运算符优先级（括号最低，×÷高于+-）
    private int getOpPriority(char op) {
        if (op == '(' || op == ')') return 0;
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return -1;
    }
}