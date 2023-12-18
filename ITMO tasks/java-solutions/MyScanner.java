import java.io.*;
import java.util.Arrays;

public class MyScanner {
    private Reader in;

    public enum Mode {
        INTEGER, WORD
    }

    private Mode mode;

    private MyScanner() {
        getDelim();
    }

    public static MyScanner scannerFromIStream(final InputStream inputStream, final Mode mode) {
        MyScanner scanner = new MyScanner();
        scanner.in = new InputStreamReader(inputStream);
        scanner.mode = mode;
        scanner.updateBuffer();
        return scanner;
    }

    public static MyScanner scannerFromFile(final String filename, final Mode mode) throws FileNotFoundException {
        MyScanner scanner = new MyScanner();
        scanner.in = new FileReader(filename);
        scanner.mode = mode;
        scanner.updateBuffer();
        return scanner;
    }

    public static MyScanner scannerFromString(final String string, final Mode mode) {
        MyScanner scanner = new MyScanner();
        scanner.in = new StringReader(string);
        scanner.mode = mode;
        scanner.updateBuffer();
        return scanner;
    }

    public void close() throws IOException {
        in.close();
    }

    private boolean isWord(final char c) {
        return c == '\'' ||
                Character.isLetter(c) ||
                Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    char delim;

    public void getDelim() {
        String sep = System.lineSeparator();
        if (sep.length() == 1 && (int)sep.charAt(0) == 13) {
            delim = sep.charAt(0);
        }else{
            delim = '\n';
        }
    }

    private boolean isNumber(final char c) {
        return Character.isDigit(c) || c == '-' || c == '+';
    }

    private boolean isDelimiter(final char c) {
        if (mode == Mode.INTEGER) {
            return (Character.isWhitespace(c) || c == delim);
        } else {
            return !isWord(c);
        }
    }

    private int bufPtr = 0;
    private int bufEnd = 0;
    private char[] buffer = new char[2];

    private int lineCount = 1;

    public int getLineCount() {
        return lineCount;
    }

    private void updateBuffer() {
        if (bufPtr >= bufEnd) {
            buffer = Arrays.copyOf(buffer, buffer.length * 2);
            try {
                bufEnd = in.read(buffer);
            } catch (IOException ex) {
                System.out.println("Something bad happened while trying to read " + ex.getMessage());
            }
            bufPtr = 0;
        }
    }

    private void skip() {
        int currSym = 0;
        while (bufEnd != -1) {
            currSym = buffer[bufPtr];
            if (!isDelimiter((char) currSym)) {
                return;
            }
            if (currSym == delim) {
                lineCount++;
            }
            bufPtr++;
            updateBuffer();
        }
        if ((char) currSym == delim)
            lineCount--;
    }

    public boolean isEOF() {
        return bufEnd == -1;
    }

    private StringBuilder parseNext() {
        StringBuilder token = new StringBuilder();
        skip();
        while (bufEnd != -1) {
            int currSym = buffer[bufPtr];
            if (isDelimiter((char) currSym)) {
                return token;
            }
            token.append((char) currSym);
            bufPtr++;
            updateBuffer();
        }
        return token;
    }

    boolean hasNextInt() {
        skip();
        return bufEnd != -1 && isNumber(buffer[bufPtr]);
    }

    boolean hasNextWord() {
        skip();
        return bufEnd != -1 && isWord(buffer[bufPtr]);
    }

    public int nextInt() {
        StringBuilder input = parseNext();
        int n = input.length();
        input.setCharAt(n - 1, Character.toLowerCase(input.charAt(n - 1)));
        boolean sign = input.charAt(0) == '-';
        int base = input.charAt(n - 1) == 'o' ? 8 : 10;
        return Integer.parseUnsignedInt(input,
                (sign == true ? 1 : 0),
                (base == 8 ? n - 1 : n), base) * (sign == true ? -1 : 1);
    }

    public String nextWord() {
        String word = parseNext().toString();
        if (word.isEmpty()) {
            throw new NumberFormatException("Emptiness is not a word!");
        }
        for (int i = 0; i < word.length(); i++) {
            if (!isWord(word.charAt(i))) {
                throw new NumberFormatException("Expected a word, but found: '" + word + "'");
            }
        }
        return word;
    }
}
