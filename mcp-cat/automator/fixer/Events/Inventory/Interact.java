package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ContainerPlayer;

public class Interact extends InventoryEvent {
    long waitTime = 0;

    public Interact(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void execute() throws CatException {
        if (state == 0) {
            t0 = System.currentTimeMillis();
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);

            state++;
        } else if (state == 1) {
            if (!(player.openContainer instanceof ContainerPlayer)) {
                //KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                result = true;
                isReady = true;
            } else if (System.currentTimeMillis() > t0 + waitTime) {
                result = false;
                isReady = true;
            }
        }
    }
}
