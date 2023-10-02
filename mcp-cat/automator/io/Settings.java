package net.automator.io;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static Map<String, String> settings;

    private static final Map<String, String> defaults = new HashMap<String, String>() {
        {
            put("DEBUG", "true");
            put("TELEGRAM_SUPPORT", "false");
            put("REQUIRE_POTION", "false");
            put("MELODY", "false");
            put("CHECK_ITEM_DROP", "false");
        }
    };

    public static void init() {
        settings = Config.loadSettings(Config.settingsName);
        if (settings.isEmpty()) {
            settings = defaults;
        }
    }

    public static String get(String key) {
        return settings.getOrDefault(key, "");
    }

    public static String put(String key, String value) {
        return settings.put(key, value);
    }

    public static String toggle(String key){
        if (Settings.get(key).equals("true")) {
            Settings.put(key, "false");
        } else {
            Settings.put(key, "true");
        }
        return Settings.get(key);
    }

    public static Map<String, String> getSettings() {
        return settings;
    }
}
