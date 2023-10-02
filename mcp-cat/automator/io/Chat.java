package net.automator.io;

import net.automator.Client;
import net.automator.Events.MessageReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Chat {
    static Minecraft mc = Minecraft.getMinecraft();
    static String name = "Cat client: ";

    public static String getLastMessage() {
        return lastMessage;
    }

    static String lastMessage;

    public static void print(String s) {
        mc.thePlayer.addChatMessage(new ChatComponentText(name + s));
    }

    public static void onMessageReceived(String message) {
        lastMessage = message;
        Client.onEvent(new MessageReceivedEvent(message));
    }
}
