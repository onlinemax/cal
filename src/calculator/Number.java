package calculator;

import java.math.BigDecimal;

public class Number {
    BigDecimal number;
    public static Number getFromInt(int a){
        return new Number(Integer.toString(a));
    }

    public Number(String s){
        number = new BigDecimal(s);
    }
    public Number(BigDecimal n){
        number = n;
    }
    public Number(Number copy){
        number = new BigDecimal(copy.number.toString());
    }
    
    @Override
    public String toString() {
        return number.toString();
    }
}
