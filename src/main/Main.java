package main;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import calculator.Calculator;
import calculator.ConstantNumber;
import calculator.DefaultCalculator;
import calculator.Number;
public class Main{
    public static void main(String[] args){
        Calculator cal = DefaultCalculator.getInstance(20);
        Number c = cal.sin(cal.divide(ConstantNumber.PI,cal.divide(Number.getFromInt(4), Number.getFromInt(3))));
        System.out.printf("%s\n", c.toString());
        MathContext mc = new MathContext(20, RoundingMode.HALF_EVEN);
        System.out.printf("%s\n", BigDecimal.ONE.divide(BigDecimal.valueOf(2).sqrt(mc), mc).toString().substring(0, 20));

    }
}