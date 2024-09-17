package calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class DefaultCalculator implements Calculator{
    private int precision;
    private int sums = 18;
    private DefaultCalculator(int precision){
        this.precision = precision;
    }
    
    private static DefaultCalculator cal;
    public static DefaultCalculator getInstance(int precision){
        if (cal == null)
            cal = new DefaultCalculator(precision);
        return cal;
    }
    @Override
    public Number add(Number a, Number b) {
        return new Number(a.number.add(b.number));
    }

    @Override
    public Number substract(Number a, Number b) {
        // TODO Auto-generated method stub
        return new Number(a.number.subtract(b.number));
    }

    @Override
    public Number multiply(Number a, Number b) {
        // TODO Auto-generated method stub
        return new Number(a.number.multiply(b.number, new MathContext(precision, RoundingMode.HALF_EVEN)));
    }

    @Override
    public Number divide(Number a, Number b) {
        MathContext mc = new MathContext(precision);
        return new Number(a.number.divide(b.number, mc));
    }
    private int verifyPrecision(int lastPrecision, BigDecimal last, BigDecimal now){
        String a = last.toString(), b = now.toString();
        for (int i = lastPrecision; i < Math.min(a.length(), b.length()); i++){
            if (a.charAt(i) != b.charAt(i))
                return i;   
        }
        if (a.length() != b.length()){
            return Math.min(a.length(), b.length());
        }
        return precision;
    }
    @Override
    public Number sin(Number a) {
        final MathContext mc = new MathContext(precision + 10, RoundingMode.HALF_EVEN);
        BigDecimal x = new BigDecimal(a.number.toString(), mc);
        boolean negative = false;
        if (x.signum() == -1){
            x = x.negate();
            negative ^= true;
        }
        final BigDecimal fullCircle = BigDecimal.valueOf(360), halfCircle = BigDecimal.valueOf(180), quarterCircle = BigDecimal.valueOf(90); 
        // Change it to degree and to 
        x = x.multiply(halfCircle.divide(ConstantNumber.PI.number, mc), mc).remainder(fullCircle);
        if (x.compareTo(halfCircle) >= 0){
            x = x.subtract(halfCircle, mc);
            negative ^= true;
        }
        if (x.compareTo(quarterCircle) == 1)
            x = halfCircle.subtract(x, mc);
        x = x.multiply(ConstantNumber.PI.number.divide(halfCircle, mc), mc);
        BigDecimal[] terms = new BigDecimal[sums + 1];
        BigDecimal factorial = BigDecimal.valueOf(1);
        BigDecimal lastSolution = new BigDecimal(0, mc);
        BigDecimal solution = new BigDecimal(0, mc);
        int localPrecision = 0;
        for (int i = 0; i < terms.length; i++){
            terms[i] = x.pow(i * 2, mc);
        }
        boolean good = false;
        for (int i = 0; !good; i += 2 * sums){
            BigDecimal adder = terms[sums - 1];
            for (int j = i + sums * 2 - 2; j > i; j-= 2){
                int n = j * (j + 1);
                if (n == 0)
                    n = 1;
                factorial = factorial.multiply(BigDecimal.valueOf(n));
                adder  = adder.divide(BigDecimal.valueOf(n), mc).negate();
            adder = adder.add(terms[(j - i) / 2 - 1], mc);
            }
            solution = solution.add(adder.multiply(x, mc), mc);
            localPrecision = verifyPrecision(localPrecision, lastSolution, solution);
            good = localPrecision >= precision;
            factorial = factorial.multiply(BigDecimal.valueOf((i + 2 * sums) * (i + 2 * sums + 1)));
            x = x.multiply(terms[sums], mc).divide(factorial, mc);
            lastSolution = new BigDecimal(solution.toString());
        }
        if (negative)
            solution = solution.negate();
        return new Number(solution.toString().substring(0, precision));
    }
    @Override
    public Number cos(Number a) {
        MathContext mc = new MathContext(precision + 10, RoundingMode.HALF_EVEN);
        return new Number(BigDecimal.ONE.subtract(this.sin(a).number.pow(2, mc)).sqrt(mc));
    }
    
}