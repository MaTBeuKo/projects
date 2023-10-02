//package net.automator.fixer.Events.Inventory;
//
//import net.automator.Client;
//import net.automator.fixer.Exception.CatException;
//import net.automator.fixer.Manager;
//import net.automator.modules.InventoryManager;
//import net.minecraft.client.Minecraft;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class FindAttributePrice extends InventoryEvent {
//
//    List<String> attributes = new ArrayList<String>();
//    List<String> antiAttributes = new ArrayList<String>();
//    String request;
//
//    public FindAttributePrice(String request, List<String> attributes) {
//        this.request = request;
//        this.attributes = attributes;
//    }
//
//    public FindAttributePrice(String request, List<String> attributes, List<String> antiAttributes) {
//        this.request = request;
//        this.attributes = attributes;
//        this.antiAttributes = antiAttributes;
//    }
//
//    @Override
//    public void execute() throws CatException {
//        InventoryManager ah = InventoryManager.getInstance();
//        if (state == 0) {
//            Click.waitForButton = 1000;
//            EditSign.waitTime = 1000;
//            Minecraft.getMinecraft().thePlayer.sendChatMessage("/ah");
//            Manager.add(new Click("Auctions Browser"));
//            state++;
//        } else if (state == 1) {
//            if (Manager.empty()) {
//                state++;
//            }
//        } else if (state == 2) {
//            if (InventoryManager.getInstance().find(item ->item != null && ah.getItemDescription(item).contains("reset settings")) != -1) {
//                Manager.add(new Click("Reset Settings"));
//            }
//            Manager.add(new Click("Search"));
//            Manager.add(new EditSign(request));
//            Manager.add(new Click("Sort"));
//            state++;
//        } else if (state == 3) {
//            if (Manager.empty()) {
//                state++;
//            }
//        } else if (state == 4) {
//            int id = ah.find(item -> {
//                if (item != null) {
//                    for (String s : attributes) {
//                        if (!ah.getItemDescription(item).contains(s)) {
//                            return false;
//                        }
//                    }
//                    for (String s : antiAttributes) {
//                        if (ah.getItemDescription(item).contains(s)) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//                return false;
//            });
//            if (id != -1) {
//                String text = ah.getItemDescription(ah.getItem(id));
//                Pattern pattern = Pattern.compile("now: (.*?) ");
//                Matcher matcher = pattern.matcher(text);
//                if (matcher.find()) {
//                    String priceUnformatted = text.substring(matcher.start(), matcher.end());
////                    String priceFormatted = priceUnformatted.replaceAll(",", "");
////                    long price = Long.parseLong(priceFormatted);
//                    Client.notify("Price is " + priceUnformatted + " row: " + (id / 9 + 1 - 1) + " column: " + (id % 9 + 1 - 2));
//                } else {
//                    Client.notify("Matcher did not find price of item! " + text);
//                }
//                isReady = true;
//            } else {
//                if (ah.find(item -> item != null && ah.getItemDescription(item).contains("next page")) == -1) {
//                    isReady = true;
//                    Client.notify("No lot on ah!");
//                } else {
//                    Manager.add(new Click("Next Page", 0));
//                    state++;
//                }
//            }
//        } else if (state == 5) {
//            if (Manager.empty()) {
//                state = 4;
//            }
//        }
//    }
//}
