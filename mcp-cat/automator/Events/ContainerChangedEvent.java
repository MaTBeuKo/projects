package net.automator.Events;

import net.minecraft.item.ItemStack;

public class ContainerChangedEvent extends AbstractEvent{
    public final int slot;
    public final ItemStack itemStack;
    public ContainerChangedEvent(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }
}
