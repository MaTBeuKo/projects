package net.automator.fixer.Schemes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Buy implements Callable<String> {
    private final Map<String, Long> items;

    public Buy(Map<String, Long> items) {
        this.items = items;
    }

    public Buy(String itemName, Long amount){
        items = new HashMap<>();
        items.put(itemName, amount);
    }

    public Buy(){
        items = new HashMap<>();
    }

    @Override
    public String call() throws InterruptedException {
        int failedOperations = 0;
        for (Map.Entry<String, Long> entry : items.entrySet()) {
            if (!Utils.instabuyItem(entry.getKey(), entry.getValue(), Long.MAX_VALUE)) {
                failedOperations++;
            }
        }
        return "successful: " + (items.entrySet().size() - failedOperations) + ", failed: " + failedOperations;
    }
}