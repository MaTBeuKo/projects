package net.automator.fixer.Schemes;

import net.automator.fixer.Events.Inventory.*;
import net.automator.fixer.MetaEvents.LongEvent;

import java.util.AbstractMap;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleGuests implements Callable<String> {
    private final Integer numberOfGuests;
    private final Long priceLimit;


    public HandleGuests() {
        numberOfGuests = Utils.numberOfGuests;
        priceLimit = Utils.guestsPriceLimit;
    }

    public static AbstractMap.SimpleEntry<String, Long> parseRequiredItem(String rawItemDescription) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("Items Required:\n (.*?)\n");
        Matcher matcher = pattern.matcher(rawItemDescription);
        if (matcher.find()) {
            String nameAndPrice = matcher.group(1).trim();
            String lastToken = nameAndPrice.split(" ")[nameAndPrice.split(" ").length - 1];
            Long amount = 1L;
            if (lastToken.startsWith("x")) {
                amount = Long.parseLong(lastToken.substring(1));
                nameAndPrice = nameAndPrice.substring(0, nameAndPrice.lastIndexOf(" "));
            }
            return new AbstractMap.SimpleEntry<>(nameAndPrice, amount);
        }
        throw new IllegalArgumentException("Couldn't match against this input: " + rawItemDescription);
    }

    @Override
    public String call() throws InterruptedException {
        int handled = 0;
        int accepted = 0;
        LongEvent.perform(new ChangeHotbarSlot(7));
        for (int i = 0; i < numberOfGuests; i++) {
            if ((boolean) LongEvent.perform(new Interact(10000))) {
                String formattedItemDescription = (String) LongEvent.perform(new GetItemDescription("Accept Offer"));
                LongEvent.perform(new CloseGui());
                AbstractMap.Entry<String, Long> entry = parseRequiredItem(formattedItemDescription);
                boolean bought = Utils.instabuyItem(entry.getKey(), entry.getValue(), priceLimit);
                LongEvent.perform(new Interact(10000));
                if (bought) {
                    LongEvent.perform(new Click("Accept Offer"));
                    accepted++;
                } else {
                    LongEvent.perform(new Click("Refuse Offer"));
                }
                handled++;
            } else {
                break;
            }
        }
        return "Guests handled: " + handled + ", offers accepted: " + accepted;
    }
}
