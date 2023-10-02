package net.automator.modules;

import net.automator.Events.AbstractEvent;
import net.automator.Events.ContainerChangedEvent;
import net.automator.fixer.Events.Inventory.Click;
import net.automator.fixer.Events.Wait;
import net.automator.fixer.Manager;
import net.automator.io.Chat;
import net.automator.io.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class InventoryManager extends AbstractModule {

    private static InventoryManager instance;

    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    public static String parseFormatted(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            if (s.charAt(i) == '§') {
                i += 2;
            } else {
                res.append(s.charAt(i));
                i++;
            }
        }
        return res.toString();
    }

    public int countItem(String name) {
        int amount = 0;
        for (ItemStack item : mc.thePlayer.inventory.mainInventory) {
            if (item != null && (item.getUnlocalizedName().contains(name) || (item.hasDisplayName() && item.getDisplayName().contains(name)))) {
                amount += item.stackSize;
            }
        }
        return amount;
    }

    public ItemStack getItem(int slot) {
        return mc.thePlayer.openContainer.getSlot(slot).getStack();
    }

    public String getItemDescription(ItemStack item) {
        return parseFormatted(getItemDescriptionCaseSensitive(item)).toLowerCase();
    }

    public String getItemDescriptionCaseSensitive(ItemStack item) {
        List<String> list = item.getTooltip(mc.thePlayer, false);
        StringBuilder res = new StringBuilder();
        for (String s : list) {
            res.append(s);
            res.append("\n");
        }
        return res.toString();
    }

    public String getItemDescription(int slotId) {
        if (slotId == -1) {
            return "";
        }
        return getItemDescription(getItem(slotId));
    }

    //traverses through container, returns slot with item name, or -1 if not found
    public int find(String name) {
        if (mc.thePlayer.openContainer != null) {
            for (int i = 0; i < mc.thePlayer.openContainer.inventorySlots.size() - 45; i++) {
                if (mc.thePlayer.openContainer.getSlot(i).getStack() == null) {
                    continue;
                }
                String desc = getItemDescription(mc.thePlayer.openContainer.getSlot(i).getStack());
                if (desc.contains(name.toLowerCase())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int find(Predicate<ItemStack> comp) {
        if (mc.thePlayer.openContainer != null) {
            for (int i = 0; i < mc.thePlayer.openContainer.inventorySlots.size() - 36; i++) {
                ItemStack item = mc.thePlayer.openContainer.getSlot(i).getStack();
                if (item != null && comp.test(item)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    public static int delay = 200;
    long t0 = -1;
    public static long lastContainerChange = 0;

    @Override
    public void onEvent(AbstractEvent event) {
        if (event instanceof ContainerChangedEvent) {
            lastContainerChange = System.currentTimeMillis();
            if (t0 == -1) {
                t0 = System.currentTimeMillis();
            }
            if (Settings.get("MELODY").equals("true")) {
                for (int i = 37 - 9; i <= 37 + 7 - 9; i++) {
                    ItemStack item = mc.thePlayer.openContainer.getSlot(i).getStack();
                    if (item != null && item.hasDisplayName()) {
                        String name = item.getDisplayName();
                        if (name.contains("Click!") && !name.startsWith("§7", 4)) {
                            Manager.add(new Wait((System.currentTimeMillis() - t0) / 2));
                            Manager.add(new Click(i, 3));
                            Chat.print(name);
                        }
                    }
                }
                t0 = System.currentTimeMillis();
            }
        }
    }
}
