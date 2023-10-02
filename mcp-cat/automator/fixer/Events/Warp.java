package net.automator.fixer.Events;

import net.automator.Client;
import net.automator.fixer.Exception.WarpException;
import net.automator.macros.Location;
import net.automator.utility.Time;
import net.minecraft.client.Minecraft;


public class Warp extends Event {
    private final Location location;

    private final long waitTime;

    public Warp(Location location) {
        this.location = location;
        waitTime = Time.medium();
    }

    public String locationToCommand(Location location) throws WarpException {
        if (location == Location.GARDEN) {
            return "/warp garden";
        } else if (location == Location.LOBBY) {
            return "/lobby";
        } else if (location == Location.HOME) {
            return "/warp home";
        } else if (location == Location.SKYBLOCK){
            return "/skyblock";
        }
        throw new WarpException("Unknown location");
    }
    int timesTried = 0;

    int maxTries = 5;

    @Override
    public void execute() throws WarpException {
        if (timesTried >= maxTries) {
            throw new WarpException("Unable to warp for " + maxTries + " times");
        }
        if (state == 0) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer == null){
                throw new WarpException("player was null");
            }
            mc.thePlayer.sendChatMessage(locationToCommand(location));
            state++;
            t0 = System.currentTimeMillis();
            Client.notify("Trying to warp to " + location.toString().toLowerCase());
        } else if (state == 1) {
            if (System.currentTimeMillis() > t0 + waitTime) {
                state++;
            }
        } else if (state == 2) {
            Location current = Client.getCurrentLocation();
            if (current == location ||
                    (location == Location.SKYBLOCK && Client.getCurrentLocation() != Location.LOBBY)) {
                Client.notify("Location: " + current.name().toLowerCase() + " as expected. Warped successfully");
                isReady = true;
            } else {
                state = 0;
                Client.notify("Location: " + current.name().toLowerCase() + ", expected: " + location.name().toLowerCase() + ". Warp failed.");
                timesTried++;
            }
        }
    }
}
