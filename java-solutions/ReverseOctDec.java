import java.util.ArrayList;
import java.util.List;

public class ReverseOctDec {
    public static void main(String[] args) {
        List<IntList> myArrays = new ArrayList<>();
        MyScanner in = MyScanner.scannerFromIStream(System.in, MyScanner.Mode.INTEGER);
        int longestRowSize = 0;
        int prevLineCount = 0;

        while (!in.isEOF() || prevLineCount < in.getLineCount()) {
            boolean hasInt = in.hasNextInt();
            for (; prevLineCount < in.getLineCount(); prevLineCount++) {
                myArrays.add(new IntList());
            }
            if (hasInt) {
                myArrays.get(prevLineCount - 1).add(in.nextInt());
            }
        }
        for (int i = myArrays.size() - 1; i >= 0; i--) {
            for (int j = myArrays.get(i).size() - 1; j >= 0; j--) {
                System.out.print(myArrays.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
