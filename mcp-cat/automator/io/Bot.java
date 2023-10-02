package net.automator.io;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import net.automator.Client;
import net.automator.Events.NewMessageEvent;
import net.automator.modules.Cpm;
import net.minecraft.client.Minecraft;

import java.time.Instant;

public class Bot {
    private static final TelegramBot bot = new TelegramBot(Settings.get("TG_API_KEY"));

    private Bot() {
    }

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static long beginTime;

    public static void startListening() {
        if (Settings.get("TELEGRAM_SUPPORT").equals("false")) {
            return;
        }
        beginTime = Instant.now().getEpochSecond();
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update == null || update.message() == null || update.message().date().longValue() < beginTime) {
                    continue;
                }
                if (update.message().chat().id() != Integer.parseInt(Settings.get("TG_CHAT_ID"))) {
                    continue;
                }
                handleCommand(update.message().text());
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public static void stopListening() {
        bot.removeGetUpdatesListener();
    }

    private static void handleCommand(String command) {
        if (Settings.get("TELEGRAM_SUPPORT").equals("false")) {
            return;
        }
        if (command.equals("/status")) {
            sendMessage("Im working!");
            if (mc.thePlayer != null) {
                sendMessage("cpm: " + Cpm.getInstance().getCpm());
            }
        } else {
            Client.onEvent(new NewMessageEvent(command));
        }
    }

    public static void sendMessage(String text) {
        if (Settings.get("TELEGRAM_SUPPORT").equals("false")) {
            return;
        }
        SendMessage message = new SendMessage(Settings.get("TG_CHAT_ID"), text);
        bot.execute(message);
    }
}