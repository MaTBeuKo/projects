package expression;

public abstract class BinaryExpression extends AbstractExpression {
    protected AbstractExpression left;
    protected AbstractExpression right;
    protected char operator;

    abstract protected int calculate(int firstOperand, int secondOperand);

    abstract protected double calculate(double firstOperand, double secondOperand);

    private void wrap(StringBuilder string) {
        string.insert(0, '(');
        string.append(')');
    }

    @Override
    public int evaluate(int x) {
        return calculate(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double x) {
        return calculate(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public String toString() {
        String aString = left.toString();
        String bString = right.toString();
        return ("(" + aString + " " + operator + " " + bString + ")");
    }

    @Override
    public int hashCode() {
        return (left.hashCode() * 12 + right.hashCode() * 7) * operator;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BinaryExpression)) {
            return false;
        }
        return hashCode() == object.hashCode();
    }

    @Override
    public String toMiniString() {
        StringBuilder leftString = new StringBuilder(left.toMiniString());
        StringBuilder rightString = new StringBuilder(right.toMiniString());
        if (left.priority < priority) {
            wrap(leftString);
        }
        if (right.priority < priority ||
                this instanceof Subtract && right.priority == priority ||
                this instanceof Divide && right.priority == priority ||
                this instanceof Multiply && right instanceof Divide) {
            wrap(rightString);
        }
        return leftString + " " + operator + " " + rightString;
    }
}
