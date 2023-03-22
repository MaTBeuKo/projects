package expression;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("No arguments provided!");
            return;
        }
        int x = Integer.parseInt(args[0]);
        Expression expr = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")),
                new Multiply(new Const(2), new Variable("x"))), new Const(1));
        System.out.println(expr.evaluate(x));
    }
}