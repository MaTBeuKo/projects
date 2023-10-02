package net.automator.fixer.Schemes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CreateBuyOrder implements Callable<String> {

    private final Map<String, Long> items;
    private final boolean stopBuying;

    public CreateBuyOrder() {
        items = Utils.itemsToOrder;
        stopBuying = Utils.stopBuying;
    }

    public CreateBuyOrder(String item, Long amount) {
        items = new HashMap<>();
        items.put(item, amount);
        stopBuying = Utils.stopBuying;
    }

    @Override
    public String call() throws InterruptedException {
        if (stopBuying) {
            return "stop buying mode on, canceled";
        }
        for (Map.Entry<String, Long> entry : items.entrySet()) {
            Utils.createBuyOrder(entry.getKey(), entry.getValue());
        }
        return "";
    }
}
