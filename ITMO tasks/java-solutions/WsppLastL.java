import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

public class WsppLastL {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Not enough arguments!");
            return;
        }
        String inpFileName = args[0], outFileName = args[1];
        MyScanner in;
        try {
            in = MyScanner.scannerFromFile(inpFileName, MyScanner.Mode.WORD);
        } catch (FileNotFoundException ex) {
            System.out.println("File: " + inpFileName + " Doesn't exists!" + ex.getMessage());
            return;
        }
        Map<String, IntList> map = new LinkedHashMap<>();
        Map<String, Integer> wordCount = new HashMap<>();
        Map<String, Integer> lastEntry = new LinkedHashMap<>();
        int prevLinesCount = in.getLineCount();
        boolean hasNextWord = in.hasNextWord();
        for (int i = 1; hasNextWord || !lastEntry.isEmpty(); i++, hasNextWord = in.hasNextWord()) {
            if (prevLinesCount < in.getLineCount() || !hasNextWord) {
                prevLinesCount = in.getLineCount();
                i = 1;
                for (var entry : lastEntry.entrySet()) {
                    if (map.containsKey(entry.getKey())) { // NOTE лишний поход в мапу
                        IntList wordIndexes = map.get(entry.getKey());
                        wordIndexes.add(entry.getValue());
                    } else {
                        map.put(entry.getKey(), new IntList(new int[]{entry.getValue()}));
                    }
                }
                if (!hasNextWord) {
                    break;
                }
                lastEntry.clear();
            }
            String word = in.nextWord().toLowerCase();
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            lastEntry.put(word, i);
        }
        in.close();
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outFileName));
            for (var s : map.entrySet()) {
                writer.write(s.getKey() + ' ' + wordCount.get(s.getKey()) + ' ');
                for (int j = 0; j < s.getValue().size(); ++j) {
                    writer.write(s.getValue().get(j) + (j == s.getValue().size() - 1 ? "" : " "));
                }
                writer.newLine();
            }
            writer.close();
        } catch (Exception ex) {
            System.out.println("Something went wrong while writing to " + outFileName);
            System.out.println(ex.getMessage() + ex.getCause());
        }
    }
}
