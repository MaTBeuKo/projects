public class Sum {
    static int parseNumber(String input){
            int base = 10;
            if (input.toLowerCase().charAt(input.length()-1) == 'o'){
                base = 8;
            }
            return (Integer.parseInt(input, base));
    }

    static int getSum(String input){
        String buffer = "";
        int sum = 0;
        for (int i = 0; i < input.length(); i++){
            char currChar = input.charAt(i);
            if (!Character.isWhitespace(currChar)){
                buffer += currChar;
            }else if (buffer.length() > 0){
                sum += parseNumber(buffer);
                buffer = "";
            }
        }
        if (buffer.length() > 0){
            sum += parseNumber(buffer);
        }
        return sum;
    }

    public static void main(String args[]){
        int sum = 0;
        for (String arg : args){
            sum += getSum(arg);
        }
        System.out.println(sum);
    }
}
