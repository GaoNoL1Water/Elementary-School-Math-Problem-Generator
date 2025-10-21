package com.arithmetic;

public class Fraction {
    private int numerator;   // 分子
    private int denominator; // 分母

    // 3.3节原有代码（构造方法、加减乘除）
    public Fraction(int numerator, int denominator) {
        super();
        this.denominator = denominator;
        this.numerator = numerator;
        Appointment(); // 初始化时自动约分
    }

    public Fraction(int numerator) {
        this.denominator = 1;
        this.numerator = numerator;
    }

    // 加法（原有代码）
    public Fraction add(Fraction r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b + denominator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator, newDenominator);
    }

    // 减法（原有代码）
    public Fraction sub(Fraction r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b - denominator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator, newDenominator);
    }

    // 乘法（原有代码）
    public Fraction muti(Fraction r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * a;
        int newDenominator = denominator * b;
        return new Fraction(newNumerator, newDenominator);
    }

    // 除法（原有代码）
    public Fraction div(Fraction r) {
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b;
        int newDenominator = denominator * a;
        return new Fraction(newNumerator, newDenominator);
    }

    // ---------------------- 补充缺失方法 ----------------------
    // 1. 约分（核心：分子分母同除最大公约数）
    public void Appointment() {
        int gcd = gcd(Math.abs(numerator), denominator);
        numerator /= gcd;
        denominator /= gcd;
        // 确保分母为正（如分子负、分母负时，负号移到分子）
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    // 辅助：求最大公约数（辗转相除法）
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // 2. 转换为带分数格式（如5/2 → "2'1/2"，3/5 → "3/5"，4/1 → "4"）
    public String transferFraction() {
        if (denominator == 1) {
            return String.valueOf(numerator); // 整数
        }
        int integer = numerator / denominator; // 带分数整数部分
        int remainder = numerator % denominator; // 分子余数
        if (integer == 0) {
            return remainder + "/" + denominator; // 真分数（无整数部分）
        } else {
            return integer + "'" + remainder + "/" + denominator; // 带分数
        }
    }

    // ---------------------- Getter方法（原有代码缺失，必须补全） ----------------------
    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}