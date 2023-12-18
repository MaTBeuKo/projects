package expression;

public class Variable extends UnaryExpression {
    String name;

    public Variable(String name) {
        this.name = name;
        priority = 3;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> {
                System.out.println("Wrong variable name: " + name);
                assert (false);
                yield -1;
            }
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof Variable)) {
            return false;
        }
        return hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public double evaluate(double x) {
        return x;
    }
}
