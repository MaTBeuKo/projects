package net.automator.fixer.Schemes;

import net.automator.fixer.Events.Inventory.Click;
import net.automator.fixer.Events.Inventory.CloseGui;
import net.automator.fixer.Events.Inventory.OpenChatMenu;
import net.automator.fixer.MetaEvents.LongEvent;

import java.util.concurrent.Callable;

public class ClaimAndCancel implements Callable<String> {
    @Override
    public String call() throws InterruptedException {
        if (!(boolean) LongEvent.perform(new OpenChatMenu("/bz"))) return "failed to open /bz";
        if (!(boolean) LongEvent.perform(new Click("manage orders"))) return "failed to click manage orders";
        int claimed = 0;
        while ((boolean) LongEvent.perform(new Click("click to claim"))) {
            claimed++;
        }
        int canceled = 0;
        while ((boolean) LongEvent.perform(new Click("click to view options"))) {
            while ((boolean) LongEvent.perform(new Click("cancel order"))) {
                canceled++;
            }
        }
        LongEvent.perform(new CloseGui());
        return "Claimed: " + claimed + ", Cancelled: " + canceled;
    }
}
