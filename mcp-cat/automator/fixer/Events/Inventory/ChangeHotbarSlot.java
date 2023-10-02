package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.minecraft.item.ItemStack;

public class ChangeHotbarSlot extends InventoryEvent {
    /**
     * slot - 0..8
     */
    public ChangeHotbarSlot(int slot) {
        this.slot = slot;
    }

    private final int slot;

    /**
     * untested
     *
     * @param itemName
     */
    public ChangeHotbarSlot(String itemName) {
        for (int i = 28; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack.hasDisplayName() && stack.getDisplayName().contains(itemName)) {
                slot = i;
                return;
            }
        }
        slot = 0;
    }

    @Override
    public void execute() throws CatException {
        mc.thePlayer.inventory.currentItem = slot;
        result = true;
        isReady = true;
    }
}
