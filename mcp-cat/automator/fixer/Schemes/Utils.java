package net.automator.fixer.Schemes;

import net.automator.Client;
import net.automator.fixer.Events.Inventory.*;
import net.automator.fixer.MetaEvents.LongEvent;
import net.automator.io.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Map<String, Long> itemsToOrder = new HashMap<>();
    public static List<String> itemsToSell = new ArrayList<>();
    public static Integer numberOfGuests = 5;
    public static Long guestsPriceLimit = 260000L;

    public static boolean stopBuying = false;
    public static final String itemsFileName = "items.txt";

    public static void setDefaults() {
        try (Scanner sc = new Scanner(Files.newBufferedReader(Paths.get(Config.path + itemsFileName)))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\s", 2);
                long amount = Long.parseLong(parts[0]);
                String itemName = parts[1];
                itemsToOrder.put(itemName, amount);
                itemsToSell.add(itemName);
            }
        } catch (IOException ex) {
            Client.notify(ex.getMessage() + " while reading from " + itemsFileName);
        }
    }

    public static Long parsePrice(String rawPriceString) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("Price: (.*?) coins");
        Matcher matcher = pattern.matcher(rawPriceString);
        if (matcher.find()) {
            String priceWithDot = matcher.group(1).replaceAll(",", "");
            long price;
            if (priceWithDot.contains(".")) {
                priceWithDot = priceWithDot.substring(0, priceWithDot.indexOf('.'));
                price = Long.parseLong(priceWithDot) + 1;
            } else {
                price = Long.parseLong(priceWithDot);
            }
            return price;
        }
        throw new IllegalArgumentException("Couldn't match against " + rawPriceString);
    }

    public static boolean findItem(String itemName) throws InterruptedException {
        if (!(boolean) LongEvent.perform(new OpenChatMenu("/bz"))) return false;
        if (!(boolean) LongEvent.perform(new Click("Search"))) return false;
        if (!(boolean) LongEvent.perform(new EditSign(itemName))) return false;
        if (!(boolean) LongEvent.perform(new Click(itemName, true))) return false;
        return true;
    }

    public static boolean createBuyOrder(String itemName, long amount) throws InterruptedException {
        if (!findItem(itemName)) return false;
        if (!(boolean) LongEvent.perform(new Click("create buy order"))) return false;
        if (!(boolean) LongEvent.perform(new Click("custom amount"))) return false;
        if (!(boolean) LongEvent.perform(new EditSign(String.valueOf(amount)))) return false;
        if (!(boolean) LongEvent.perform(new Click("top order +0.1"))) return false;
        if (!(boolean) LongEvent.perform(new Click("buy order"))) return false;
        if (!(boolean) LongEvent.perform(new CloseGui())) return false;
        return true;
    }

    public static boolean createSellOffer(String itemName) throws InterruptedException {
        if (!findItem(itemName)) return false;
        if (!(boolean) LongEvent.perform(new Click("create sell offer"))) return false;
        if (!(boolean) LongEvent.perform(new Click("best offer -0.1"))) {
            LongEvent.perform(new CloseGui());
            return false;
        }
        if (!(boolean) LongEvent.perform(new Click("sell offer"))) return false;
        return true;
    }

    public static boolean instabuyItem(String itemName, long amount, long priceLimit) throws InterruptedException {
        if (!findItem(itemName)) return false;
        if (!(boolean) LongEvent.perform(new Click("buy instantly"))) return false;
        if (!(boolean) LongEvent.perform(new Click("custom amount"))) return false;
        if (!(boolean) LongEvent.perform(new EditSign(String.valueOf(amount)))) return false;
        String description = (String) LongEvent.perform(new GetItemDescription("custom amount"));
        if (description.equals("")) return false;
        long price = parsePrice(description);
        if (price <= priceLimit) {
            if (!(boolean) LongEvent.perform(new Click("custom amount"))) return false;
            LongEvent.perform(new CloseGui());
            return true;
        }
        LongEvent.perform(new CloseGui());
        return false;
    }
}
