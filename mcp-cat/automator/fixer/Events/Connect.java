package net.automator.fixer.Events;

import net.automator.io.Bot;
import net.automator.Client;
import net.automator.fixer.Exception.CatException;
import net.automator.fixer.Exception.ConnectException;
import net.automator.utility.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connect extends Event {
    private String name = "hypixel";
    private String address = "hypixel.net";

    private final long waitTime;

    public Connect() {
        waitTime = Time.medium();
    }

    public Connect(String name, String address) {
        this.address = address;
        waitTime = Time.small();
    }

    private static boolean isReachable(String addr, int timeOutMillis) {
        try (Socket soc = new Socket()) {
            soc.connect(new InetSocketAddress(addr, 443), timeOutMillis);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static void disconnect() {
        if (NetHandlerPlayClient.isConnected()) {
            mc.displayGuiScreen(new GuiIngameMenu());
            mc.theWorld.sendQuittingDisconnectingPacket();
            mc.loadWorld((WorldClient) null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    int pingTries = 0;
    int maxPingTries = 20;
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void execute() throws CatException {
        if (pingTries >= maxPingTries) {
            state = 0;
            pingTries = 0;
            throw new ConnectException("Couldn't ping to 8.8.8.8 for " + pingTries + " times");
        }
        if (state == 0) {
            disconnect();
            state++;
        }
        if (state == 1) {
            Client.notify("Trying to ping 8.8.8.8");
            if (isReachable("8.8.8.8", 1000)) {
                state++;
                Client.notify("Successfully pinged 8.8.8.8");
            } else {
                pingTries++;
            }
        } else if (state == 2) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), new ServerData(name, address)));
            state++;
            t0 = System.currentTimeMillis();
            Client.notify("Trying to connect to " + name);
        } else if (state == 3) {
            if (System.currentTimeMillis() > t0 + waitTime) {
                state++;
            }
        } else if (state == 4) {
            if (NetHandlerPlayClient.isConnected()) {
                isReady = true;
                Bot.sendMessage("Successfully connected to " + name);
            } else {
                state = 0;
                pingTries = 0;
                throw new ConnectException("Couldn't connect to " + name);
            }
        }
    }
}
