package expression;

abstract public class BaseParser {
    protected CharSource source = null;
    protected final char END = '\0';
    protected char ch;
    protected char take() {
        char res = ch;
        ch = source.hasNext() ? source.next() : END;
        return res;
    }

    protected boolean take(char expected) {
        if (ch == expected) {
            take();
            return true;
        } else {
            return false;
        }
    }
    protected boolean expect(String expected){
        for (int i = 0; i < expected.length(); i++){
            if (!take(expected.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public char get(){
        return ch;
    }
    public boolean between(char min, char max){
        return min <= ch && ch <= max;
    }
    protected void skipWhitespaces() {
        while(Character.isWhitespace(ch)){
            take();
        }
    }


}
