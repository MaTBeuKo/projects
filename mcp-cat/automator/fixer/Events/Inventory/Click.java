package net.automator.fixer.Events.Inventory;

import net.automator.Client;
import net.automator.fixer.Exception.CatException;
import net.automator.modules.InventoryManager;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Click extends InventoryEvent {

    private Predicate<ItemStack> comparator;
    private int clickType = 0;
    private int slot = -1;
    private boolean fromContainer;

    public Click(Predicate<ItemStack> comp, int clickType, boolean fromContainer) {
        this.comparator = comp;
        this.clickType = clickType;
        this.fromContainer = fromContainer;
    }

    public Click(int slot, int clickType) {
        this.slot = slot;
        this.clickType = clickType;
    }

    /**
     * Any appearance, case-insensitive
     */
    public Click(String name) {
        comparator = x -> InventoryManager.getInstance().getItemDescription(x).contains(name.toLowerCase());
        fromContainer = true;
        clickType = 4;
    }

    /**
     * First line of description equals to name, case-sensitive
     */
    public Click(String name, boolean exact) {
        comparator = x -> name.equals(InventoryManager.parseFormatted(InventoryManager.getInstance().getItemDescriptionCaseSensitive(x)).split("\\R")[0].trim());
        fromContainer = true;
        clickType = 4;
    }

    public Click(String name, Pattern pattern) {
        comparator = x -> {
            String desc = InventoryManager.getInstance().getItemDescription(x);
            return pattern.matcher(name).find();
        };
        fromContainer = true;
        clickType = 4;

    }


    public Click(String name, int clickType) {
        comparator = x -> InventoryManager.getInstance().getItemDescription(x).contains(name.toLowerCase());
        fromContainer = true;
        this.clickType = clickType;
    }

    public static long waitForButton = 1500;


    @Override
    public void execute() throws CatException {
        try {
            if (t0 == 0) {
                t0 = System.currentTimeMillis();
            }
            InventoryManager menu = InventoryManager.getInstance();
            int slot = menu.find(comparator);
            if (slot != -1) {
                mc.playerController.windowClick(player.openContainer.windowId, slot, 0, clickType, mc.thePlayer);
                result = true;
                isReady = true;
            } else if (System.currentTimeMillis() >= t0 + waitForButton) {
                result = false;
                isReady = true;
            }
        } catch (Exception ex) {
            result = false;
            isReady = true;
            Client.notify(ex.getMessage());
            System.out.println((Arrays.toString(ex.getStackTrace()) + ex.getMessage()));
        }
    }
}
