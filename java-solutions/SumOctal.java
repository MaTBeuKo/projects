public class SumOctal {
    static int parseNumber(String input) {
        int base = 10;
        boolean positive = true;
        if (input.length() > 0 && input.charAt(0) == '-') {
            positive = false;
            input = input.substring(1, input.length());
        }
        if (input.toLowerCase().endsWith("o")) {
            base = 8;
            input = input.substring(0, input.length() - 1);

        }
        int returnValue = Integer.parseUnsignedInt(input, base);
        return (positive ? returnValue : -returnValue);
    }

    static int getSum(String input) {
        int sum = 0;
        for (int i = 0, j = 0; j < input.length(); i++, j++) {
            while (j < input.length() && !Character.isWhitespace(input.charAt(j))) {
                j++;
            }
            if (j > i) {
                sum += parseNumber(input.substring(i, j));
                i = j;
            }
        }
        return sum;
    }

    public static void main(String args[]) {
        int sum = 0;
        for (String arg : args) {
            sum += getSum(arg);
        }
        System.out.println(sum);
    }
}
