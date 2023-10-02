package net.automator.ui;

import net.automator.Client;
import net.automator.Macros;
import net.automator.modules.Cpm;
import net.automator.modules.MacrosPlayer;
import net.automator.modules.MacrosRecorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class HUD {
    Minecraft mc = Minecraft.getMinecraft();
    ScaledResolution sr = new ScaledResolution(mc, mc.displayHeight, mc.displayWidth);
    FontRenderer fr = mc.fontRendererObj;

    ArrayList<String> printer = new ArrayList<>();

    public void print(String s) {
        printer.add(s);
    }

    public void model() {
        if (MacrosRecorder.getInstance().isToggled()) {
            print("Recording");
        }
        if (MacrosPlayer.getInstance().isToggled()) {
            print("Playing");
        }
        print("Blocks per minute: " + Cpm.getInstance().getCpm());
        print("Min blocks per minute: " + Cpm.getInstance().getMincpm());
    }

    public void draw() {

        printer.clear();
        model();
        for (int i = 0; i < printer.size(); i++) {
            mc.fontRendererObj.drawString(printer.get(i), 4, 4 + i * fr.FONT_HEIGHT, Color.BLACK.getRGB());
        }
        MacrosBox box = new MacrosBox(4, 30, Client.getMacrosList());
        box.draw();
    }

    public class MacrosBox {
        int x;
        int y;
        ArrayList<Macros> macrosList;

        MacrosBox(int x, int y, ArrayList<Macros> macrosList) {
            this.x = x;
            this.y = y;
            this.macrosList = macrosList;
        }

        int marg = 4;

        public void draw() {
            if (macrosList.isEmpty()) {
                return;
            }
            int maxWidth = 0;

            for (Macros macros : macrosList) {
                maxWidth = Math.max(maxWidth, fr.getStringWidth(macros.getName()));
            }
            Gui.drawRect(x, y, x + maxWidth + marg * 2, y + (macrosList.size()) * (fr.FONT_HEIGHT + 1) + marg, 0x90000000);
            Gui.drawRect(x + marg - 2, y + 3 + Client.getSelectedMacros() * (fr.FONT_HEIGHT + 1),
                    x + marg + fr.getStringWidth(macrosList.get(Client.getSelectedMacros()).getName()) + 2, y + 3 + Client.getSelectedMacros() * (fr.FONT_HEIGHT + 1) +
                            fr.FONT_HEIGHT, 0xFF36afc7);

            for (int i = 0; i < macrosList.size(); i++) {
                mc.fontRendererObj.drawString(macrosList.get(i).getName(), x + marg, y + 3 + i * (fr.FONT_HEIGHT + 1), Color.RED.getRGB());
            }

        }
    }
}
