package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ContainerPlayer;

public class OpenMenu extends InventoryEvent {

    @Override
    public void execute() throws CatException {
        if (state == 0) {
            //player.inventory.currentItem = 8;
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
            state++;
            t0 = System.currentTimeMillis();
        } else if (state == 1) {
            if(System.currentTimeMillis() > t0 + 250) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
                state++;
            }
        }else if (state == 2){
            isReady = (!(player.openContainer instanceof ContainerPlayer));
        }
    }
}
