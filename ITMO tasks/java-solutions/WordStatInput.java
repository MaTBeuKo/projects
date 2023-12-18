import java.io.*;
import java.util.LinkedHashMap;

public class WordStatInput {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }
        String inpFileName = args[0], outFileName = args[1];
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        MyScanner scanner = MyScanner.scannerFromFile(inpFileName, MyScanner.Mode.WORD);
        while (scanner.hasNextWord()) {
            String word = scanner.nextWord().toLowerCase();
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }
        scanner.close();
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outFileName));
            for (var s : map.entrySet()) {
                writer.write(s.getKey() + ' ' + s.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println("Something went wrong while writing to " + outFileName);
            System.out.println(exception.getMessage() + exception.getCause());
        }
    }
}
