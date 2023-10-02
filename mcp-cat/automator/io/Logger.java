package net.automator.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static void print(String string) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            String fileName = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now) + ".txt";
            Files.write(
                    Paths.get(Config.path + fileName),
                    (dtf.format(now) + " " + string + '\n').getBytes(),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException ignored) {
        }
    }
}
