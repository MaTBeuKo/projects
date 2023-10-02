package net.automator;

import net.automator.Events.*;
import net.automator.fixer.Events.MacroCheck;
import net.automator.fixer.Manager;
import net.automator.fixer.Schemes.Utils;
import net.automator.io.*;
import net.automator.macros.Location;
import net.automator.modules.*;
import net.automator.ui.HUD;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final MacrosRecorder recorder = MacrosRecorder.getInstance();
    private static final MacrosPlayer player = MacrosPlayer.getInstance();

    private static ArrayList<Macros> macrosList = new ArrayList<>();

    private static Map<String, MacrosGroup> groups = new HashMap<>();
    private static Map<String, Chain> chains = new HashMap<>();

    public static Chain getChain(String name) {
        return chains.getOrDefault(name, new Chain(1));
    }

    public static void putChain(String name, Chain chain) {
        chains.put(name, chain);
    }

    public static MacrosGroup getGroup(String name) {
        return groups.getOrDefault(name, new MacrosGroup());
    }

    public static void putGroup(String name, MacrosGroup group) {
        groups.put(name, group);
    }

    private static CommandHandler handler;

    public static HUD hud = new HUD();

    public static int getSelectedMacros() {
        return selectedMacros;
    }

    public static void setSelectedMacros(int selectedMacros) {
        Client.selectedMacros = selectedMacros;
    }

    private static int selectedMacros = -1;

    public static ArrayList<Macros> getMacrosList() {
        return macrosList;
    }


    private static final ArrayList<AbstractModule> modules = new ArrayList();

    public static ArrayList<AbstractModule> getModules() {
        return modules;
    }

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Location getCurrentLocation() {
        NetHandlerPlayClient playClient = Minecraft.getMinecraft().getNetHandler();
        Map<UUID, NetworkPlayerInfo> players = playClient.getPlayerInfoMap();
        for (Map.Entry<UUID, NetworkPlayerInfo> entry : players.entrySet()) {
            String name = mc.ingameGUI.getTabList().func_175243_a(entry.getValue());
            if (name.equals("§r§b§lArea: §r§7Private Island§r")) {
                return Location.HOME;
            } else if (name.equals("§r§b§lArea: §r§7Hub§r")) {
                return Location.HUB;
            } else if (name.equals("§r§b§lArea: §r§7Garden§r")) {
                return Location.GARDEN;
            }
        }
        return Location.LOBBY;
    }

    public static void save() throws IOException {
        Config.saveSettings(Config.settingsName, Settings.getSettings());
        Config.saveData(Config.macrosListName, macrosList);
        Config.saveData(Config.groupsName, groups);
        Config.saveData(Config.chainsName, chains);
    }

    public static void onStartup() {
        Settings.init();
        // mc.getTextureManager().bindTexture(new ResourceLocation("/picBig.png"));
        if (Settings.get("TELEGRAM_SUPPORT").equals("true")) {
            Bot.startListening();
        }
        hud.model();
        try {
            macrosList = (ArrayList<Macros>) Config.loadData(Config.macrosListName);
        } catch (IOException | ClassNotFoundException ex) {
            Client.notify("Couldn't load macros list from disk " + ex.getMessage());
        }
        try {
            groups = (HashMap<String, MacrosGroup>) Config.loadData(Config.groupsName);
        } catch (IOException | ClassNotFoundException ex) {
            Client.notify("Couldn't load groups from disk " + ex);
        }
        try {
            chains = (HashMap<String, Chain>) Config.loadData(Config.chainsName);
        } catch (IOException | ClassNotFoundException ex) {
            Client.notify("Couldn't load chains from disk " + ex);
        }
        if (macrosList.size() > 0) {
            selectedMacros = 0;
        }
        handler = CommandHandler.getInstance();
        recorder.setKey(Keyboard.KEY_M);
        player.setKey(Keyboard.KEY_P);
        modules.add(recorder);
        modules.add(player);
        modules.add(handler);
        modules.add(Cpm.getInstance());
        modules.add(InventoryManager.getInstance());
        Utils.setDefaults();
        if (!Settings.get("DEBUG").equals("true")) {
            Manager.addFullyReset();
        }
    }

    private static boolean inMenu = false;

    public static boolean inMenu() {
        return inMenu;
    }

    public static long worldSwitchedTick = -1;

    private static void handleNewMessage(MessageReceivedEvent event) {
        if (event.getMessage().contains("Evacuating")) {
            Manager.addRestart();
            Client.notify("Server reboot");
        }
    }

    public static String dump() {
        StringBuilder msg = new StringBuilder("***DUMP***\n");
        StringBuilder exceptions = new StringBuilder();
        try {
            msg.append("Current screen: ").append(mc.currentScreen.toString());
        } catch (Exception ex) {
            exceptions.append(ex.getMessage()).append("\n").append(Arrays.toString(ex.getStackTrace())).append("\n");
        }
        msg.append("\n");
        try {
            msg.append("Current location: ").append(Client.getCurrentLocation()).append("\n");
        } catch (Exception ex) {
            exceptions.append(ex.getMessage()).append("\n").append(Arrays.toString(ex.getStackTrace())).append("\n");
        }
        msg.append("***DUMP***\n");
        return msg + exceptions.toString();
    }

    public static synchronized void notify(String message) {
        Bot.sendMessage(message);

        Logger.print(message);
        if (mc.thePlayer != null) {
            Chat.print(message);
        }
    }

    public static StringBuilder buffer = new StringBuilder();

    public static void onEvent(AbstractEvent event) {
        if (event instanceof DisconnectedEvent) {
            if (recorder.isToggled()) {
                recorder.disable();
            }
            if (player.isToggled()) {
                player.disable();
            }
            if (!Manager.isConnecting() && !(Settings.get("DEBUG").equals("true"))) {
                Manager.addFullyReset();
            }
        }
        if (event instanceof MessageReceivedEvent) {
            handleNewMessage((MessageReceivedEvent) event);
            return;
        }
        if (event instanceof WorldSwitchEvent) {
            worldSwitchedTick = getTick();
            return;
        }
        if (event instanceof BlockBreakEvent) {
            player.onEvent(event);
        }
        if (recorder.isToggled() && event instanceof IOEvent) {
            recorder.onEvent(event);
            if (!recorder.isToggled()) {
                macrosList.add(recorder.getMacros());
                if (selectedMacros == -1) {
                    selectedMacros = 0;
                }
            }
            return;
        } else if (event instanceof KeyPressEvent && ((KeyPressEvent) event).getKey() == player.getKey() && !player.isToggled()) {
            player.setup(macrosList.get(selectedMacros));
            player.onEvent(event);
            return;
        }
        for (AbstractModule m : modules) {
            m.onEvent(event);
        }
        if (event.getType() == EventType.KEY_PRESS) {
            int keyNum = ((KeyPressEvent) event).getKey();
            if (keyNum == Keyboard.KEY_UP && selectedMacros != -1) {
                selectedMacros--;
                if (selectedMacros < 0) {
                    selectedMacros = macrosList.size() - 1;
                }
            } else if (keyNum == Keyboard.KEY_DOWN && selectedMacros != -1) {
                selectedMacros = (selectedMacros + 1) % macrosList.size();
            } else if (keyNum == Keyboard.KEY_HOME) {
                if (!inMenu) {
                    inMenu = true;
                    mc.mouseHelper.ungrabMouseCursor();
                } else {
                    inMenu = false;
                    mc.mouseHelper.grabMouseCursor();
                }
            }
        }
    }

    static int cnt = 0;

    static float rotationYaw = 0;
    static float rotationPitch = 0;

    static int buttonPressed = 0;

    public static float getYaw() {
        return rotationYaw;
    }

    public static float getPitch() {
        return rotationPitch;
    }

    public static int getButton() {
        return buttonPressed;
    }

    private static void checkForMacroCheck() {
        Block blockUnderneath = (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock());
        ItemStack itemInHand = mc.thePlayer.getHeldItem();
        if (player.isToggled()) {
            if (mc.theWorld != null && (blockUnderneath.getUnlocalizedName().equals("tile.bedrock") || (
                    itemInHand != null && itemInHand.getItem().getUnlocalizedName().toLowerCase().contains("map")))) {
                player.disable();
                Manager.add(new MacroCheck());
            }
        }
    }

    static long lastSuccessfulBreak = 0;
    static int lastSeenAmount = -1;

    static public void tick() {
        if (mc.thePlayer == null)
            return;
        if (CommandHandler.localTestFuture != null && CommandHandler.localTestFuture.isDone()) {
            try {
                Object result = CommandHandler.localTestFuture.get();
                Client.notify((String) result);
            } catch (Exception ex) {
                Client.notify("Future ex: " + ex.getMessage());
            } finally {
                CommandHandler.localTestFuture = null;
            }
        }
        checkForMacroCheck();
        cnt++;
        if (player != null && player.isToggled()) {
            player.onTick();
            if (player.executingFuture == null) {
                Cpm.getInstance().onTick();
            }
        }
        if (player != null && player.isToggled() && Settings.get("CHECK_ITEM_DROP").equals("true") && player.executingFuture == null) {
            if (!MacrosPlayer.getInstance().isBreaking()) {
                lastSuccessfulBreak = System.currentTimeMillis();
            } else {
                int currentAmount = InventoryManager.getInstance().countItem("melon");
                if (currentAmount != lastSeenAmount) {
                    lastSuccessfulBreak = System.currentTimeMillis();
                }
                long delay = System.currentTimeMillis() - lastSuccessfulBreak;
                if (delay > 1500) {
                    MacrosPlayer.getInstance().disable();
                    Manager.add(new MacroCheck());
                }
                lastSeenAmount = currentAmount;
            }
        } else {
            lastSuccessfulBreak = 0;
            lastSeenAmount = -1;
        }
    }

    public static ArrayList<ItemStack> oldInv;

    static public void onSmallTick() {
        //if (Settings.get("MELODY").equals("true")) {
        if (mc.thePlayer == null || mc.thePlayer.openContainer == null) {
            return;
        }
        List current = mc.thePlayer.openContainer.inventorySlots;
        if (current.size() <= 45) {
            return;
        }
        ArrayList<ItemStack> inv = new ArrayList<>(current.size());
        for (int i = 0; i < current.size(); i++) {
            inv.add(((Slot) current.get(i)).getStack());
        }
        if (Client.oldInv == null) {
            Client.oldInv = new ArrayList<ItemStack>(inv.size());
            for (int i = 0; i < inv.size(); i++) {
                Client.oldInv.add((ItemStack) inv.get(i));
            }
        }
        boolean changed = false;
        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i) != null && Client.oldInv.get(i) != null &&
                    !ItemStack.areItemStacksEqual(inv.get(i), Client.oldInv.get(i))) {
                changed = true;
                break;
            }
        }
        for (int i = 0; i < inv.size(); i++) {
            ItemStack item = inv.get(i);
            if (item != null) {
                item = item.copy();
            }
            Client.oldInv.set(i, item);
        }
        if (changed) {
            //Client.notify("Update: " + System.currentTimeMillis()/10);
            Client.onEvent(new ContainerChangedEvent(-1, null));
        }
    }
    //}

    static public long getTick() {
        return cnt;
    }
}
