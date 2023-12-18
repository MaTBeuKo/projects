package expression;

public class StringCharSource implements CharSource {
    final private String string;
    private int pos = 0;

    public StringCharSource(final String string) {
        this.string = string;
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }
}
