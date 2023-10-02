package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.minecraft.inventory.ContainerPlayer;

public class OpenChatMenu extends InventoryEvent{
    private final long waitTime = 1000;
    private final String command;
    public OpenChatMenu(String command) {
        this.command = command;
    }

    @Override
    public void execute() throws CatException {
        if (state == 0){
            mc.thePlayer.sendChatMessage(command);
            t0 = System.currentTimeMillis();
            state++;
        }else {
            if (!(player.openContainer instanceof ContainerPlayer)){
                result = true;
                isReady = true;
            }
            if (System.currentTimeMillis() > t0 + waitTime){
                result = false;
                isReady = true;
            }
        }
    }
}
