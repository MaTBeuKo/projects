package net.automator.io;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

    public static final String path = System.getProperty("user.home") + "\\cat\\";

    public static final File directory = new File(path);
    public static final String macrosListName = "macros.cat";
    public static final String groupsName = "groups.cat";
    public static final String chainsName = "chains.cat";

    public static final String settingsName = "settings.txt";

    private static void makeDir() throws IOException {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Can't create directory: " + directory);
            }
        }
    }

    public static void saveData(String fileName, Object data) throws IOException {

        makeDir();
        try (FileOutputStream stream = new FileOutputStream(path + fileName)) {
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(data);
        }
    }

    public static Object loadData(String fileName) throws IOException, ClassNotFoundException {
        Object data;
        FileInputStream stream = new FileInputStream(path + fileName);
        data = (new ObjectInputStream(stream)).readObject();
        return data;
    }

    public static void saveSettings(String fileName, Map<String, String> settings) throws IOException {
        makeDir();
        try (PrintWriter printWriter = new PrintWriter(path + fileName)) {
            for (Map.Entry<String, String> pair : settings.entrySet()) {
                printWriter.println(pair.getKey() + " " + pair.getValue());
            }
        }
    }

    public static Map<String, String> loadSettings(String fileName) {
        Map<String, String> result = new HashMap<>();
        try (Scanner scanner = new Scanner(new FileInputStream(path + fileName))) {
            while (scanner.hasNext()) {
                result.put(scanner.next(), scanner.next());
            }
        } catch (FileNotFoundException ex) {

        }
        return result;
    }
}
