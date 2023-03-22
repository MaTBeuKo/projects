import java.util.Scanner;
import java.util.Arrays;

public class ReverseSum {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] linesLength = new int[0];
        int[] linesSums = new int[0];
        int[] columnsSums = new int[0];
        int[][] numbers = new int[0][0];
        int lineOfElement = 0;
        while (in.hasNextLine()) {
            if (lineOfElement > linesSums.length - 1) {
                linesLength = Arrays.copyOf(linesLength, linesLength.length * 3 / 2 + 1);
                numbers = Arrays.copyOf(numbers, numbers.length * 3 / 2 + 1);
                linesSums = Arrays.copyOf(linesSums, linesSums.length * 3 / 2 + 1);
            }
            String currentLine = in.nextLine();
            numbers[lineOfElement] = new int[1];
            int columnOfElement = 0;
            int sumOfLine = 0;
            Scanner line = new Scanner(currentLine);
            while (line.hasNextInt()) {
                if (columnOfElement > numbers[lineOfElement].length - 1) {
                    numbers[lineOfElement] =
                            Arrays.copyOf(numbers[lineOfElement], numbers[lineOfElement].length * 3 / 2 + 1);
                }
                if (columnOfElement > columnsSums.length - 1) {
                    columnsSums = Arrays.copyOf(columnsSums, columnsSums.length * 3 / 2 + 1);
                }
                int a = line.nextInt();
                sumOfLine += a;
                columnsSums[columnOfElement] += a;
                linesLength[lineOfElement] += 1;
                numbers[lineOfElement][columnOfElement] = a;
                columnOfElement++;
            }
            linesSums[lineOfElement] = sumOfLine;
            lineOfElement++;
            line.close();
        }
        in.close();
        for (int i = 0; i < lineOfElement; i++) {
            for (int j = 0; j < linesLength[i]; j++) {
                System.out.print(linesSums[i] + columnsSums[j] - numbers[i][j] + " ");
            }
            System.out.println();
        }
    }
}








