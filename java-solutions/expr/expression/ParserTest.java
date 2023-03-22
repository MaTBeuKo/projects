package expression;

import base.Selector;

import java.util.function.Consumer;


/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    private static final TripleParser PARSER = new ExpressionParser();
    private static final Consumer<ParserTester> TRIPLE = Operations.kind(
            TripleExpression.KIND,
            (expr, variables) -> PARSER.parse(expr)
    );
    public static final Selector SELECTOR = Selector.composite(ParserTest.class, ParserTester::new, "easy", "hard")
            .variant("Base", TRIPLE, Operations.ADD, Operations.SUBTRACT, Operations.MULTIPLY, Operations.DIVIDE, Operations.NEGATE)
            .variant("SetClear", Operations.SET, Operations.CLEAR)
            .variant("Count", Operations.COUNT)
            .variant("GcdLcm", Operations.GCD, Operations.LCM)
            .variant("Reverse", Operations.REVERSE)
            .selector();

    private ParserTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
