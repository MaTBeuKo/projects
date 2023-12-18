package expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

public class MyTester {
    private static void valid(AbstractExpression good, ExpressionParser parser, String s){
        Assertions.assertEquals(good, parser.parse(s));
    }
    @Test
    public void test0() {
        StringCharSource source = new StringCharSource("10+20");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new Add(new Const(10), new Const(20));
        valid(good, parser, "10+20");
    }
    @Test
    public void test1() {
        StringCharSource source = new StringCharSource("10 - 20");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new Subtract(new Const(10), new Const(20));
        valid(good, parser, "10-20");
    }
    @Test
    public void test2() {
        StringCharSource source = new StringCharSource("100-200+300  -400");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new Subtract(new Add(new Subtract(new Const(100), new Const(200)),  new Const(300)), new Const(400));
        valid(good, parser, "100-200+300  -400");
    }
    @Test
    public void test3(){
        StringCharSource source = new StringCharSource("x*x-2*x+1");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")),
                new Multiply(new Const(2), new Variable("x"))), new Const(1));
        valid(good, parser, "x*x-2*x+1");
    }
    @Test
    public void test4(){
        StringCharSource source = new StringCharSource("-2*1");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new UnaryMinus(new Multiply(new Const(2),new Const(1)));
        valid(good, parser, "-2*1");
    }
    @Test
    public void test5(){
        StringCharSource source = new StringCharSource("1");
        ExpressionParser parser = new ExpressionParser();
        AbstractExpression good = new Const(1);
        valid(good, parser, "1");
    }
}
