package net.automator.modules;

import net.automator.Chain;
import net.automator.Client;
import net.automator.Events.AbstractEvent;
import net.automator.Events.NewMessageEvent;
import net.automator.Macros;
import net.automator.MacrosGroup;
import net.automator.fixer.Events.Warp;
import net.automator.fixer.Manager;
import net.automator.fixer.Schemes.*;
import net.automator.io.Chat;
import net.automator.macros.Location;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Future;

public class CommandHandler extends AbstractModule {
    private static CommandHandler instance = null;
    private final Minecraft mc;
    private final ArrayList<Macros> macrosList;


    private CommandHandler() {
        mc = Minecraft.getMinecraft();
        macrosList = Client.getMacrosList();
    }

    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }


    @Override
    public void onEvent(AbstractEvent event) {
        if (!(event instanceof NewMessageEvent)) {
            return;
        }
        try {
            handle((NewMessageEvent) event);
        } catch (NoSuchElementException | NumberFormatException ex) {
            Chat.print(ex.getMessage());
        }
    }

    String readTillTheEnd(Scanner sc) {
        StringBuilder s = new StringBuilder();
        while (sc.hasNext()) {
            s.append(sc.next());
            s.append(' ');
        }
        return s.toString().trim();
    }

    public static Future<?> localTestFuture = null;

    private void handle(NewMessageEvent message) {
        String s = message.getMessage();
        if (!s.startsWith("//")) {
            return;
        }
        message.cancel();
        s = s.substring(2);
        Scanner sc = new Scanner(s);
        String word = sc.next();
        if (word.equals("delete")) {
            if (macrosList.size() > 0) {
                String name = macrosList.get(Client.getSelectedMacros()).getName();
                macrosList.remove(Client.getSelectedMacros());
                Client.setSelectedMacros(Client.getSelectedMacros() - 1);
                if (Client.getSelectedMacros() == -1 && macrosList.size() > 0) {
                    Client.setSelectedMacros(0);
                }
                Chat.print("Macros \"" + name + "\" deleted");
            } else {
                Chat.print("Nothing to delete!");
            }
        } else if (word.equals("chain")) {
            String chainName = sc.next();
            String operation = sc.next();
            Chain chain = Client.getChain(chainName);
            if (operation.equals("remove")) {
                int id = sc.nextInt();
                chain.removeEvent(id);
                Client.putChain(chainName, chain);
                Chat.print("Event removed from chain \"" + chainName + "\"");
            } else if (operation.equals("add")) {
                String eventType = sc.next();
                if (eventType.equals("random")) {
                    String macrosGroupName = sc.next();
                    int repeatCount = sc.nextInt();
                    chain.addRandomMacros(Client.getGroup(macrosGroupName), repeatCount);
                    Client.putChain(chainName, chain);
                    Chat.print("random from group " + macrosGroupName + " added to the " + chainName);
                } else if (eventType.equals("macros")) {
                    int macrosId = Client.getSelectedMacros();
                    int repeatCount = sc.nextInt();
                    chain.addMacros(Client.getMacrosList().get(macrosId), repeatCount);
                    Client.putChain(chainName, chain);
                    Chat.print(Client.getMacrosList().get(macrosId).getName() + " added to the " + chainName);
                } else if (eventType.equals("interaction")) {
                    String callableClassName = sc.next();
                    try {
                        chain.addInteraction(callableClassName, sc.nextInt());
                        Client.putChain(chainName, chain);
                        Chat.print("Interaction" + callableClassName + " added to the " + chainName);
                    } catch (Exception ex) {
                        Client.notify("Exception adding interaction" + ex.getMessage());
                    }
                } else {
                    Client.notify("Unknown type");
                }
            } else if (operation.equals("print")) {
                Chat.print(chain.toString());
            } else if (operation.equals("repeat")) {
                int repeatCount = sc.nextInt();
                chain.setRepeatCount(repeatCount);
                Client.putChain(chainName, chain);
                Chat.print("Chain \"" + chainName + "\" repeat count set to " + repeatCount);
            }
        } else if (word.equals("help")) {
            Chat.print("Usage examples:");
            Chat.print("chain chainName add random groupName repeatCount");
            Chat.print("chain chainName add macros repeatCount");
            Chat.print("chain chainName remove eventId");
            Chat.print("group groupName add");
        } else if (word.equals("play")) {
            String chainName = sc.next();
            MacrosPlayer.getInstance().setup(Client.getChain(chainName));
            MacrosPlayer.getInstance().enable();
            Chat.print("Playing chain \"" + chainName + "\"");
        } else if (word.equals("group")) {
            String groupName = sc.next();
            String operation = sc.next();
            if (operation.equals("add")) {
                MacrosGroup group = Client.getGroup(groupName);
                group.add(Client.getMacrosList().get(Client.getSelectedMacros()));
                Client.putGroup(groupName, group);
                Chat.print(Client.getMacrosList().get(Client.getSelectedMacros()).getName() + " added to the " + groupName);
            } else if (operation.equals("remove")) {
                int id = sc.nextInt();
                MacrosGroup group = Client.getGroup(groupName);
                String macrosName = group.get(id).getName();
                group.remove(id);
                Client.putGroup(groupName, group);
                Chat.print(macrosName + " removed from the " + groupName);
            } else if (operation.equals("print")) {
                Chat.print(Client.getGroup(groupName).toString());
            }
        } else if (word.equals("current")) {
            MacrosPlayer player = MacrosPlayer.getInstance();
            if (!player.isToggled()) {
                Chat.print("Not playing any macros");
            } else {
                Chat.print("Current macros: " + player.getMacros().getName());
            }
        } else if (word.equals("restart")) {
            Manager.addRestart();
        } else if (word.equals("fr")) {
            Manager.addFullyReset();
        } else if (word.equals("save")) {
            try {
                Client.save();
                Chat.print("Saved successfully!");
            } catch (IOException ex) {
                Client.notify("Couldn't save data! " + ex.getMessage());
            }
        } else if (word.equals("rename")) {
            String newName = sc.next();
            macrosList.get(Client.getSelectedMacros()).setName(newName);
            Chat.print("Macros renamed");
        } else if (word.equals("info")) {
            Client.getCurrentLocation();
        } else if (word.equals("getItemDescription")) {
            try {
                String itemName = readTillTheEnd(sc);
                Client.notify(InventoryManager.getInstance().getItemDescription(InventoryManager.getInstance().find(itemName)));
            } catch (Throwable throwable) {
                Client.notify(throwable.getMessage());
            }
        } else if (word.equals("buy")) {
            long amount = Long.parseLong(sc.next());
            String itemName = readTillTheEnd(sc);
            localTestFuture = Client.executorService.submit(new Buy(itemName, amount));
        } else if (word.equals("handleGuests")) {
            localTestFuture = Client.executorService.submit(new HandleGuests());
        } else if (word.equals("createBuyOrder")) {
            long amount = Long.parseLong(sc.next());
            String itemName = readTillTheEnd(sc);
            localTestFuture = Client.executorService.submit(new CreateBuyOrder(itemName, amount));
        } else if (word.equals("createSellOffer")) {
            String itemName = readTillTheEnd(sc);
            localTestFuture = Client.executorService.submit(new CreateSellOffer(itemName));
        } else if (word.equals("claimAndCancel")) {
            localTestFuture = Client.executorService.submit(new ClaimAndCancel());
        } else if (word.equals("stopBuying")) {
            Utils.stopBuying = !Utils.stopBuying;
        } else if (word.equals("dump")) {
            Client.notify(Client.dump());
        } else if (word.equals("threads")) {
            Client.notify("Threads: " + Thread.activeCount());
        } else if (word.equals("clear")) {
            Manager.clear();
        } else if (word.equals("pause")) {
            MacrosPlayer.getInstance().disable();
            Manager.add(new Warp(Location.HOME));
        } else {
            Client.notify("Unknown command");
        }
    }
}