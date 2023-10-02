package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public abstract class InventoryEvent extends Event {
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
}
