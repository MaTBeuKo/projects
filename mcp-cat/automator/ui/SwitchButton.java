package net.automator.ui;

import net.minecraft.client.gui.GuiButton;

public class SwitchButton extends GuiButton {
    public final String pretty_name;
    public final String setting;
    public SwitchButton(int buttonId, int x, int y, String setting, String pretty_name, String state) {
        super(buttonId, x, y, pretty_name + ": " + state);
        this.pretty_name = pretty_name;
        this.setting = setting;
    }
    public void setState(String state){
        displayString = pretty_name + ": " + state;
    }
}
