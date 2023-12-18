package expression;

public class UnaryMinus extends UnaryExpression {
    final private AbstractExpression expression;

    public UnaryMinus(AbstractExpression expression) {
        this.expression = expression;
    }

    @Override
    public double evaluate(double x) {
        return -expression.evaluate(x);
    }

    @Override
    public int evaluate(int x) {
        return -expression.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expression.evaluate(x, y, z);
    }

    @Override
    public String toString() {
        return "-" + expression.toString();
    }
}
