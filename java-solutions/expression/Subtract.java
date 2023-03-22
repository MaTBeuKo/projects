package expression;

public class Subtract extends BinaryExpression {
    public Subtract(AbstractExpression left, AbstractExpression right) {
        this.left = left;
        this.right = right;
        operator = '-';
        priority = 1;
    }
    @Override
    protected int calculate(int firstOperand, int secondOperand) {
        return firstOperand - secondOperand;
    }

    @Override
    protected double calculate(double firstOperand, double secondOperand) {
        return firstOperand-secondOperand;
    }
}
