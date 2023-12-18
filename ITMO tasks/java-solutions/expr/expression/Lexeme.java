package expression;

public class Lexeme {
    private LexemeTypes type;
    private String value;

    public Lexeme(LexemeTypes type) {
        this.type = type;
    }

    public Lexeme(LexemeTypes type, String value) {
        this.type = type;
        this.value = value;
    }

    public LexemeTypes getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
