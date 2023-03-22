package expression;

public class Const extends UnaryExpression {
    private enum Kind {
        INTEGER, DOUBLE
    }
    private final Kind kind;
    final private Number value;
    public Const(int value) {
        this.value = value;
        kind = Kind.INTEGER;
        priority = 3;
    }

    public Const(double value) {
        this.value = value;
        kind = Kind.DOUBLE;
        priority = 3;
    }

    @Override
    public int evaluate(int x) {
        if (kind == Kind.DOUBLE){
            System.out.println("Error, expression has double const, but evaluating integer value");
            assert(false);
        }
        return value.intValue();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof Const)) {
            return false;
        }
        return hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (kind == Kind.DOUBLE){
            System.out.println("Error, expression has double const, but evaluating integer value");
            assert(false);
        }
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        if (kind == Kind.INTEGER){
            System.out.println("Error, expression has integer const, but evaluating double value");
            assert(false);
        }
        return value.doubleValue();
    }
}
