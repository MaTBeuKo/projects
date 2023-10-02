package net.automator.fixer.Schemes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CreateSellOffer implements Callable<String> {
    private final List<String> items;

    public CreateSellOffer() {
        items = Utils.itemsToSell;
    }

    public CreateSellOffer(String item) {
        items = new ArrayList<>();
        items.add(item);
    }

    @Override
    public String call() throws Exception {
        for (String item : items) {
            Utils.createSellOffer(item);
        }
        return "";
    }
}
