package net.automator.fixer;

import net.automator.Client;
import net.automator.fixer.Events.*;
import net.automator.fixer.Exception.*;
import net.automator.macros.Location;
import net.automator.modules.MacrosPlayer;
import net.automator.utility.Time;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Manager {
    private static final Queue<Event> queue = new LinkedList<>();


    public static synchronized void add(Event event) {
        queue.add(event);
    }

    public static boolean isConnecting() {
        return (queue.peek() != null && (queue.peek() instanceof Connect || queue.peek() instanceof Disconnect));
    }

    public static int size() {
        return queue.size();
    }

    public static boolean empty() {
        return size() == 0;
    }

    public static void runTick() {
        try {
            if (!queue.isEmpty()) {
                Event top = queue.peek();
                synchronized (top) {
                    try {
                        top.execute();
                    } catch (WarpException ex) {
                        Client.notify(ex.getMessage());
                        addFullyReset();
                    } catch (ConnectException ex) {
                        Client.notify(ex.getMessage());
                    } catch (CpmException ex) {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                        Client.notify(ex.getMessage());
                        addFullyReset();
                    } catch (MacroException ex) {
                        Client.notify(ex.getMessage());
                        addRestart();
                        return;
                    } catch (ClickException ex) {
                        //if (!Settings.get("MELODY").equals("true")) {
                        Client.notify(ex.getMessage());
                        //}
                    } catch (CatException ex) {
                        Client.notify(Arrays.toString(ex.getStackTrace()) + ex.getMessage());
                        Minecraft.getMinecraft().shutdown();
                    }
                    if (top.ready()) {
                        queue.poll();
                        top.notify();
                    }
                }
            }
            if (!bigQueue.isEmpty()) {
                Event event = bigQueue.peek();
                event.execute();
                if (event.ready()) {
                    bigQueue.poll();
                }
            }
        } catch (Exception ex) {
            Client.notify(Arrays.toString(ex.getStackTrace()));
            Minecraft.getMinecraft().shutdown();
        }
    }

    public static void clear() {
        queue.clear();
    }

    public static void addRestart() {
        if (MacrosPlayer.getInstance().isToggled()) {
            MacrosPlayer.getInstance().disable();
        }
        queue.clear();
        add(new Wait(Time.small()));
        add(new Warp(Location.GARDEN));
        add(new PlayMacro(Client.getChain("melonfix")));
        add(new PlayMacro(Client.getChain("melon")));
    }

    public static void addFullyReset() {
        if (MacrosPlayer.getInstance().isToggled()) {
            MacrosPlayer.getInstance().disable();
        }
        queue.clear();
        add(new Connect());
        add(new Warp(Location.SKYBLOCK));
        add(new Warp(Location.GARDEN));
        add(new PlayMacro(Client.getChain("melonfix")));
        add(new PlayMacro(Client.getChain("melon")));
    }

    public static void addMacrocheck() {
        add(new PlayMacro(Client.getChain("macrocheck")));
        add(new Disconnect());
    }

    private static Queue<Event> bigQueue = new LinkedList<>();

    public static void addBigEvent(Event event) {
        bigQueue.add(event);
    }
}
//ClickItem("Compass");
//ClickItem("sword");
//while(ClickItem("sugar").failed == true){
//wait(500)
//ClickItem("sugar")
//}