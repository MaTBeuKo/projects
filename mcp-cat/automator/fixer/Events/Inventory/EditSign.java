package net.automator.fixer.Events.Inventory;

import net.automator.fixer.Exception.CatException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;

import java.io.IOException;

public class EditSign extends InventoryEvent {

    String text;
    public static long waitTime = 1500;

    public EditSign(String text) {
        this.text = text;
    }

    @Override
    public void execute() throws CatException {
        if (state == 0) {
            if (t0 == 0) {
                t0 = System.currentTimeMillis();
            }
            if (Minecraft.getMinecraft().currentScreen instanceof GuiEditSign) {
                state++;
            } else if (System.currentTimeMillis() > t0 + waitTime) {
                result = false;
                isReady = true;
            }
        } else if (state == 1) {
            if (!(mc.currentScreen instanceof GuiEditSign)) {
                result = false;
                isReady = true;
                return;
            }
            GuiEditSign gui = (GuiEditSign) mc.currentScreen;
            gui.edit(text);
            try {
                gui.close();
                result = true;
            } catch (IOException ex) {
                result = false;
            } finally {
                isReady = true;
            }
        }
    }
}
