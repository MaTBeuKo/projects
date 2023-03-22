import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Wspp {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }
        String inpFileName = args[0], outFileName = args[1];
        LinkedHashMap<String, IntList> map = new LinkedHashMap<>();
        MyScanner scanner;
        try {
            scanner = MyScanner.scannerFromFile(inpFileName, MyScanner.Mode.WORD);
        }catch (FileNotFoundException ex){
            System.out.println("File: " + inpFileName + " Doesn't exists!" + ex.getMessage());
            return;
        }
        int currIndex = 1;
        while (scanner.hasNextWord()) {
            String word = scanner.nextWord().toLowerCase();
            IntList wordIndexes = map.getOrDefault(word, new IntList());
            wordIndexes.add(currIndex);
            map.put(word, wordIndexes);
            currIndex++;
        }
        scanner.close();
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outFileName));
            for (var s : map.entrySet()) {
                writer.write(s.getKey() + ' ' + s.getValue().size() + ' ');
                for (int j = 0; j < s.getValue().size(); ++j) {
                    writer.write(s.getValue().get(j) + (j == s.getValue().size() - 1 ? "" : " "));
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println("Something went wrong while writing to " + outFileName);
            System.out.println(exception.getMessage() + exception.getCause());
        }
    }
}
