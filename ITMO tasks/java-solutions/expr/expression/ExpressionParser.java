package expression;


import java.util.ArrayList;

public class ExpressionParser extends BaseParser implements TripleParser {
    private ArrayList<Lexeme> lexemes = new ArrayList<>();
    private int pos = 0;

    private Lexeme next() {
        return lexemes.get(pos++);
    }

    //E -> T + E | T - E | T
    //T -> F * T | F / T | F
    //F -> N     | (E)


    //signed-expr --> - expr | expr
    //expr --> expr + term | term
    //term --> term * factor | factor
    //factor --> ( signed-expr ) | number
    private AbstractExpression signedE(){
        if (pos < lexemes.size() && lexemes.get(pos).getType() == LexemeTypes.EOF){
            return new Const(0);
        }
        Lexeme nextLexeme = next();
        if (nextLexeme.getType() == LexemeTypes.UNARY_MINUS){
            return new UnaryMinus(E());
        }else{
            pos--;
            return E();
        }
    }

    private AbstractExpression E() {
        AbstractExpression firstExpr = T();
        if(pos >= lexemes.size()){
            return firstExpr;
        }
        Lexeme nextLexeme = next();

        if (nextLexeme.getType() == LexemeTypes.ADD) {
            firstExpr = new Add(firstExpr, E());
        } else if (nextLexeme.getType() == LexemeTypes.SUB) {
            firstExpr = new Subtract(firstExpr, E());

        } else {
            pos--;
            return firstExpr;
        }
        return firstExpr;
    }

    private AbstractExpression T() {
        AbstractExpression firstExpr = F();
        if(pos >= lexemes.size()){
            return firstExpr;
        }
        Lexeme nextLexeme = next();

        if (nextLexeme.getType() == LexemeTypes.MUL) {
            firstExpr = new Multiply(firstExpr, T());
        } else if (nextLexeme.getType() == LexemeTypes.DIV) {
            firstExpr = new Multiply(firstExpr, T());
        } else {
            pos--;
            return firstExpr;
            //throw new IllegalArgumentException("Bad lexeme");
        }
        return firstExpr;
    }

    private AbstractExpression F() {
        Lexeme nextLexeme = next();
        if (nextLexeme.getType() == LexemeTypes.NUM) {
            return new Const(Integer.parseInt(nextLexeme.getValue()));
        } else if (nextLexeme.getType() == LexemeTypes.VAR) {
            return new Variable(nextLexeme.getValue());
        } else if (nextLexeme.getType() == LexemeTypes.L_BRACKET) {
            AbstractExpression res = signedE();
            if (next().getType() != LexemeTypes.R_BRACKET) {
                throw new IllegalArgumentException("Bad lexem");
            }
            return res;
        } else {
            throw new IllegalArgumentException("Bad lexem");
        }
    }

    private void parseLexemes() {
        Lexeme current = new Lexeme(LexemeTypes.NUM);
        Lexeme prev = null;
        while (current.getType() != LexemeTypes.EOF) {
            skipWhitespaces();
            if (between('0', '9')) {
                current = new Lexeme(LexemeTypes.NUM, parseNumber().toString());
            } else if (take('(')) {
                current = new Lexeme(LexemeTypes.L_BRACKET);
                prev = current;
            } else if (take(')')) {
                current = new Lexeme(LexemeTypes.R_BRACKET);
            } else if (ch == 'x' || ch == 'y' || ch == 'z') {
                current = new Lexeme(LexemeTypes.VAR, Character.toString(take()));
            } else if (take('-')) {
                if (prev == null || prev.getType() == LexemeTypes.L_BRACKET) {
                    current = new Lexeme(LexemeTypes.UNARY_MINUS);
                } else {
                    current = new Lexeme(LexemeTypes.SUB);
                }
            } else if (take(END)) {
                current = new Lexeme(LexemeTypes.EOF);
            } else if (take('+')) {
                current = new Lexeme(LexemeTypes.ADD);
            } else if (take('-')) {
                current = new Lexeme(LexemeTypes.SUB);
            } else if (take('/')) {
                current = new Lexeme(LexemeTypes.DIV);
            } else if (take('*')) {
                current = new Lexeme(LexemeTypes.MUL);
            } else if (take('l')) {
                if (expect("og10")) {
                    current = new Lexeme(LexemeTypes.LOG10);
                } else {
                    throw new IllegalArgumentException("Bad log10");
                }
            } else if (take('p')) {
                if (expect("ow10")) {
                    current = new Lexeme(LexemeTypes.POW10);
                } else {
                    throw new IllegalArgumentException("Bad pow10");
                }
            } else if (take('s')) {
                if (expect("et")) {
                    current = new Lexeme(LexemeTypes.SET);
                } else {
                    throw new IllegalArgumentException("Bad set");
                }
            } else if (take('c')) {
                if (expect("lear")) {
                    current = new Lexeme(LexemeTypes.CLEAR);
                } else {
                    throw new IllegalArgumentException("Bad clear");
                }
            } else if (take('c')) {
                if (expect("ount")) {
                    current = new Lexeme(LexemeTypes.COUNT);
                } else {
                    throw new IllegalArgumentException("Bad count");
                }
            } else if (take(END)) {
                current = new Lexeme(LexemeTypes.EOF);
            } else {
                throw new UnsupportedOperationException("Idk what it is");
            }
            prev = new Lexeme(LexemeTypes.NUM);
            lexemes.add(current);
        }
    }

    @Override
    public TripleExpression parse(String expression) {
        source = new StringCharSource(expression);
        pos = 0;
        take();
        parseLexemes();
        return signedE();
    }

    private Integer parseNumber() {
        StringBuilder number = new StringBuilder();
//        if (take('0')) {
////            if (between('0', '9')) {
////                throw new IllegalArgumentException("Bad null");
////            }
//        }
        do {
            number.append(take());
        } while (between('0', '9'));
        return Integer.parseInt(number.toString());
    }

}
