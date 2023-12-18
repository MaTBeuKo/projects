package expression.common.common;

import base.ExtendedRandom;
import base.Functional;
import base.Pair;
import base.TestCounter;
import expression.ToMiniString;
import expression.common.Expr;
import expression.common.ExpressionKind.Variables;
import expression.common.Generator;
import expression.common.Node;
import expression.common.NodeRenderer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TestGenerator<C> {
    private final TestCounter counter;
    private final ExtendedRandom random;

    private final expression.common.Generator<C> generator;
    private final expression.common.NodeRenderer<C> renderer;

    private final Set<String> forbidden = new HashSet<>();
    private final List<Function<List<expression.common.Node<C>>, Stream<expression.common.Node<C>>>> basicTests = new ArrayList<>();
    private final List<expression.common.Node<C>> consts;
    private final boolean verbose;

    public TestGenerator(
            final TestCounter counter,
            final ExtendedRandom random,
            final Supplier<C> constant,
            final List<C> constants,
            final boolean verbose
    ) {
        this.counter = counter;
        this.random = random;
        this.verbose = verbose;

        generator = new Generator<>(random, constant);
        renderer = new expression.common.NodeRenderer<>(this.random);

        consts = Functional.map(constants, expression.common.Node::constant);
        basicTests.add(vars -> consts.stream());
        basicTests.add(List::stream);
    }

    private <E> void test(final expression.common.Expr<C, E> expr, final Consumer<Test<C, E>> consumer) {
        consumer.accept(new Test<>(
                expr,
                renderer.render(expr, expression.common.NodeRenderer.FULL),
                renderer.render(expr, expression.common.NodeRenderer.FULL_EXTRA),
                renderer.render(expr, expression.common.NodeRenderer.MINI),
                renderer.render(expr, expression.common.NodeRenderer.SAME)
        ));
    }

    private expression.common.Node<C> c() {
        return random.randomItem(consts);
    }

    private expression.common.Node<C> v(final List<expression.common.Node<C>> variables) {
        return random.randomItem(variables);
    }

    private static <C> expression.common.Node<C> f(final String name, final expression.common.Node<C> arg) {
        return expression.common.Node.op(name, arg);
    }

    private static <C> expression.common.Node<C> f(final String name, final expression.common.Node<C> arg1, final expression.common.Node<C> arg2) {
        return expression.common.Node.op(name, arg1, arg2);
    }

    @SafeVarargs
    private void basicTests(final Function<List<expression.common.Node<C>>, expression.common.Node<C>>... tests) {
        Arrays.stream(tests).map(test -> test.andThen(Stream::of)).forEachOrdered(basicTests::add);
    }

    public void unary(final String name) {
        generator.add(name, 1);
        renderer.unary(name);
        forbidden.add(name);

        if (verbose) {
            basicTests.add(vars -> Stream.concat(consts.stream(), vars.stream()).map(a -> f(name, a)));
        } else {
            basicTests(
                    vars -> f(name, c()),
                    vars -> f(name, v(vars))
            );
        }

        final Function<List<expression.common.Node<C>>, expression.common.Node<C>> p1 = vars -> f(name, f(name, f("+", v(vars), c())));
        final Function<List<expression.common.Node<C>>, expression.common.Node<C>> p2 = vars -> f("*", v(vars), f("*", v(vars), f(name, c())));
        basicTests(
                vars -> f(name, f("+", v(vars), v(vars))),
                vars -> f(name, f(name, v(vars))),
                vars -> f(name, f("/", f(name, v(vars)), f("+", v(vars), v(vars)))),
                p1,
                p2,
                vars -> f("+", p1.apply(vars), p2.apply(vars))
        );
    }

    public void binary(final String name, final int priority) {
        generator.add(name, 2);
        renderer.binary(name, priority);
        forbidden.add(name);

        if (verbose) {
            basicTests.add(vars -> Stream.concat(consts.stream(), vars.stream().limit(3))
                            .flatMap(a -> consts.stream().map(b -> f(name, a, b))));
        } else {
            basicTests(
                    vars -> f(name, c(), c()),
                    vars -> f(name, v(vars), c()),
                    vars -> f(name, c(), v(vars)),
                    vars -> f(name, v(vars), v(vars))
            );
        }

        final Function<List<expression.common.Node<C>>, expression.common.Node<C>> p1 = vars -> f(name, f(name, f("+", v(vars), c()), v(vars)), v(vars));
        final Function<List<expression.common.Node<C>>, expression.common.Node<C>> p2 = vars -> f("*", v(vars), f("*", v(vars), f(name, c(), v(vars))));

        basicTests(
                vars -> f(name, f(name, v(vars), v(vars)), v(vars)),
                vars -> f(name, v(vars), f(name, v(vars), v(vars))),
                vars -> f(name, f(name, v(vars), v(vars)), f(name, v(vars), v(vars))),
                vars -> f(name, f("-", f(name, v(vars), v(vars)), c()), f("+", v(vars), v(vars))),
                p1,
                p2,
                vars -> f("+", p1.apply(vars), p2.apply(vars))
        );
    }

    public <E> void testBasic(final Variables<E> variables, final Consumer<Test<C, E>> consumer) {
        basicTests.forEach(test -> {
            final List<Pair<String, E>> vars = generator.variables(variables, random.nextInt(5) + 3);
            test.apply(Functional.map(vars, v -> Node.op(v.first()))).forEachOrdered(node -> {
//                counter.println(full.render(Expr.of(node, vars)));
                test(expression.common.Expr.of(node, vars), consumer);
            });
        });
    }

    public <E> void testRandom(final int denominator, final Variables<E> variables, final Consumer<Test<C, E>> consumer) {
        generator.testRandom(denominator, counter, variables, expr -> test(expr, consumer));
    }

    public String full(final expression.common.Expr<C, ?> expr) {
        return renderer.render(expr, NodeRenderer.FULL);
    }

    public <E extends ToMiniString> List<Pair<String,E>> variables(final Variables<E> variables, final int count) {
        return generator.variables(variables, count);
    }

    public static class Test<C, E> {
        public final expression.common.Expr<C, E> expr;
        public final String full;
        public final String fullExtra;
        public final String mini;
        public final String safe;

        public Test(final Expr<C, E> expr, final String full, final String fullExtra, final String mini, final String safe) {
            this.expr = expr;
            this.full = full;
            this.fullExtra = fullExtra;
            this.mini = mini;
            this.safe = safe;
        }
    }
}
