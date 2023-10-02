package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.automator.modules.InventoryManager;

public class GetItemDescription extends InventoryEvent {
    String itemName;

    public GetItemDescription(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void execute() throws CatException {
        InventoryManager manager = InventoryManager.getInstance();
        int slotId = manager.find(itemName);
        result = "";
        if (slotId != -1) {
            result = InventoryManager.parseFormatted(manager.getItemDescriptionCaseSensitive(manager.getItem(slotId)));
        }
        isReady = true;
    }
}
