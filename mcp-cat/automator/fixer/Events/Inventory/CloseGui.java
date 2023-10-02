package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;

public class CloseGui extends InventoryEvent {
    @Override
    public void execute() throws CatException {
        mc.thePlayer.closeScreen();
        result = true;
        isReady = true;
    }
}
