package expression;

import expression.TripleExpression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FunctionalInterface
public interface TripleParser {
    TripleExpression parse(String expression);
}
