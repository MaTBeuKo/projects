package expression;

public class Divide extends BinaryExpression {
    public Divide(AbstractExpression left, AbstractExpression right) {
        this.left = left;
        this.right = right;
        operator = '/';
        priority = 2;
    }

    @Override
    protected int calculate(int firstOperand, int secondOperand) {
        return firstOperand / secondOperand;
    }

    @Override
    protected double calculate(double firstOperand, double secondOperand) {
        return firstOperand / secondOperand;
    }
}
